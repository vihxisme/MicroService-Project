package com.service.order.services.impl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.events.NotificationEvent;
import com.service.events.dtos.InvenItemCheckStock;
import com.service.order.dtos.OrderDTO;
import com.service.order.dtos.TopProductDTO;
import com.service.order.entities.Order;
import com.service.order.mappers.OrderMapper;
import com.service.order.repositories.ApiClient;
import com.service.order.repositories.OrderRepository;
import com.service.order.requests.OrderItemRequest;
import com.service.order.requests.OrderRequest;
import com.service.order.requests.PaginationRequest;
import com.service.order.requests.ProdSendEmailRequest;
import com.service.order.requests.ShippingAddressRequest;
import com.service.order.resources.OrderMnResource;
import com.service.order.resources.OrderStatisticsResource;
import com.service.order.resources.TransactionResource;
import com.service.order.responses.PaginationResponse;
import com.service.order.services.interfaces.OrderInterface;
import com.service.order.utils.EventUtil;
import com.service.order.wrappers.CheckInventoryWrapper;
import com.service.order.wrappers.OrderItemReqWrapper;
import com.service.order.wrappers.OrderWrapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import com.service.events.dtos.PaymentDTO;
import com.service.order.dtos.WriteEmailDTO;
import com.service.order.resources.OrderDetailResource;
import com.service.order.resources.OrderItemProdResource;

@Service
public class OrderService implements OrderInterface {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private ApiClient apiClient;

    @Value("${rabbitmq.exchange.order}")
    private String myExchange;

    @Value("${rabbitmq.exchange.inventory}")
    private String inventoryExchange;

    @Value("${rabbitmq.exchange.payment}")
    private String paymentExchange;

    @Value("${rabbitmq.exchange.notification}")
    private String notificationExchange;

    @Value("${service.cloudinary.base-url}")
    private String imgUrlPrefix;

    private Logger logger = LoggerFactory.getLogger(OrderService.class);

    @RabbitListener(queues = "create-order:queue")
    public Order createOrderListener(OrderRequest request) {
        return createOrder(request);
    }

    private String generateOrderCode() {
        Boolean isUnique;
        Random random = new Random();
        String code;
        do {
            int randomNumberStart = random.nextInt(100, 999);
            int randomNumberMiddle = random.nextInt(1000, 9999);
            int randomNumberEnd = random.nextInt(1000, 9999);
            code = String.format("%03d-%04d-%04d", randomNumberStart, randomNumberMiddle, randomNumberEnd);
            isUnique = !orderRepository.existsByOrderCode(code);
        } while (!isUnique);

        return code;
    }

    @Override
    @Transactional
    public Order createOrder(OrderRequest request) {
        if (request.getOrderCode() == null) {
            request.setOrderCode(generateOrderCode());
        }

        Order order = orderMapper.toOrder(request);

        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrder(OrderRequest request) {
        Order existOrder = orderRepository.findById(request.getId()).orElseThrow(()
                -> new EntityNotFoundException("Order not found"));

        orderMapper.updateOrderFromRequest(request, existOrder);

        return existOrder;
    }

    @Override
    @Transactional
    public Boolean deleteOrder(UUID id) {
        Order existOrder = orderRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Order not found"));

        orderRepository.delete(existOrder);

        return true;
    }

    @Override
    public Order create(OrderWrapper wrapper) {
        OrderRequest orderRequest = wrapper.getOrderRequest();

        Order order = createOrder(orderRequest);

        continueOrderProcessing(order, wrapper);
        sendCheckInventoryTask(order, wrapper);

        return order;
    }

    @RabbitListener(queues = "check-inventory-task:queue")
    public void checkInventoryListener(CheckInventoryWrapper wrapper) {
        Order order = wrapper.getOrder();
        OrderWrapper orderWrapper = wrapper.getOrderWrapper();

        List<InvenItemCheckStock> checkStockList = orderWrapper.getOrderItemRequests().stream()
                .map(item -> InvenItemCheckStock.builder()
                .prodVariantId(item.getProdVariantId())
                .itemQuantity(item.getQuantity())
                .build())
                .collect(Collectors.toList());

        List<Integer> outOfStock = checkInventoryOrThrow(checkStockList);
        if (!outOfStock.isEmpty()) {
            handleOutOfStock(order);
            sendPaymentCancelTask(order);
            sendNotificationCancelOrderTask(order, orderWrapper);
        } else {
            sendNotificationTask(order, orderWrapper);
        }
    }

    @Transactional
    private void handleOutOfStock(Order order) {
        order.setStatus(5);
        orderRepository.save(order);
    }

    private void sendPaymentCancelTask(Order order) {
        rabbitService.sendMessage(paymentExchange, "cancel-payment", order.getId());
    }

    private void sendNotificationCancelOrderTask(Order order, OrderWrapper wrapper) {
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .email(wrapper.getShippingAddressRequest().getEmail())
                .name(wrapper.getShippingAddressRequest().getName())
                .subject(String.format("[BELLION SHOP] Rất tiếc! Đơn hàng #%s đã bị huỷ do hết hàng", order.getOrderCode()))
                .body(EventUtil.emailBodyForCancelOrder(myExchange, order, imgUrlPrefix, getProdSendEmail(wrapper.getOrderItemRequests())))
                .build();
        rabbitService.sendMessage(notificationExchange, "send-email", notificationEvent);
    }

    private void sendCheckInventoryTask(Order order, OrderWrapper wrapper) {
        CheckInventoryWrapper checkInventoryWrapper = CheckInventoryWrapper.builder()
                .order(order)
                .orderWrapper(wrapper)
                .build();
        rabbitTemplate.convertAndSend(myExchange, "check-inventory-task", checkInventoryWrapper);
    }

    private void sendOrderItemTask(Order order, OrderWrapper wrapper) {
        List<OrderItemRequest> orderItemRequest = wrapper.getOrderItemRequests().stream()
                .map(item -> OrderItemRequest.builder()
                .orderId(order.getId())
                .productId(item.getProductId())
                .prodVariantId(item.getProdVariantId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .totalPrice(item.getTotalPrice())
                .build())
                .collect(Collectors.toList());

        OrderItemReqWrapper itemWrapper = OrderItemReqWrapper.builder().orderItemRequestList(orderItemRequest).build();
        rabbitTemplate.convertAndSend(myExchange, "create-order-item", itemWrapper);
    }

    private void sendPaymentTask(Order order, OrderWrapper wrapper) {
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(wrapper.getPaymentMethod())
                .build();
        rabbitService.sendMessage(paymentExchange, "create-payment", paymentDTO);
    }

    private void sendShippingTask(Order order, OrderWrapper wrapper) {
        ShippingAddressRequest shippingRequest = wrapper.getShippingAddressRequest();
        shippingRequest.setOrderId(order.getId());
        rabbitTemplate.convertAndSend(myExchange, "create-shipping", shippingRequest);
    }

    private void sendNotificationTask(Order order, OrderWrapper wrapper) {
        ShippingAddressRequest shippingRequest = wrapper.getShippingAddressRequest();
        NotificationEvent notificationEvent = NotificationEvent.builder()
                .email(shippingRequest.getEmail())
                .name(shippingRequest.getName())
                .subject(String.format("BELLION SHOP] Xác nhận đơn hàng #%s – Cảm ơn bạn đã đặt hàng!", order.getOrderCode()))
                .body(EventUtil.emailBodyForOrder(shippingRequest.getName(), order, imgUrlPrefix, getProdSendEmail(wrapper.getOrderItemRequests())))
                .build();
        rabbitService.sendMessage(notificationExchange, "send-email", notificationEvent);
    }

    private List<WriteEmailDTO> getProdSendEmail(List<OrderItemRequest> orderItemRequests) {
        Map<String, Integer> quantityMap = orderItemRequests.stream()
                .collect(Collectors.toMap(
                        item -> item.getProductId().toString() + "_" + item.getProdVariantId().toString(),
                        OrderItemRequest::getQuantity
                ));

        List<ProdSendEmailRequest> requests = orderItemRequests.stream()
                .map(item -> ProdSendEmailRequest.builder()
                .productId(item.getProductId())
                .variantId(item.getProdVariantId())
                .build())
                .collect(Collectors.toList());

        ResponseEntity<?> response = apiClient.getProdSendEmail(requests);

        List<WriteEmailDTO> writeEmailDTO = objectMapper.convertValue(response.getBody(), new TypeReference<List<WriteEmailDTO>>() {
        });

        List<WriteEmailDTO> writeEmailDTOList = writeEmailDTO.stream()
                .<WriteEmailDTO>map(item -> WriteEmailDTO.builder()
                .name(item.getName())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .colorName(item.getColorName())
                .sizeName(item.getSizeName())
                .quantity(quantityMap.get(item.getProductId().toString() + "_" + item.getVariantId().toString()))
                .build())
                .collect(Collectors.toList());

        return writeEmailDTOList;
    }

    private void continueOrderProcessing(Order order, OrderWrapper wrapper) {
        sendOrderItemTask(order, wrapper);
        sendShippingTask(order, wrapper);
        sendPaymentTask(order, wrapper);
    }

    public List<Integer> checkInventoryOrThrow(List<InvenItemCheckStock> checkStock) {
        ResponseEntity<?> response = apiClient.checkInventoryItem(checkStock);

        Map<Integer, Boolean> resultMap = objectMapper.convertValue(response.getBody(), new TypeReference<Map<Integer, Boolean>>() {
        });

        List<Integer> outOfStock = resultMap.entrySet().stream()
                .filter(entry -> !entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return outOfStock;
    }

    @Override
    public Object submitOrder(OrderWrapper wrapper) {
        Order order = create(wrapper);
        return order;
    }

    @Override
    public PaginationResponse<OrderMnResource> getAllOrder(PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        Page<OrderMnResource> pages = orderRepository.findAllOrder(pageable);

        return PaginationResponse.<OrderMnResource>builder()
                .content(pages.getContent())
                .pageNumber(pages.getNumber())
                .pageSize(pages.getSize())
                .totalPages(pages.getTotalPages())
                .totalElements(pages.getTotalElements())
                .build();
    }

    @Override
    public OrderStatisticsResource getOrderStatistics() {
        return orderRepository.getOrdersStatistics();
    }

    @Override
    public Map<UUID, TransactionResource> caculateTransaction(List<UUID> userIds) {
        // Fetch transaction data for all users
        List<Order> transactions = orderRepository.findByUserIds(userIds);

        logger.info("transactionOrder: {}", transactions.size());

        // Group transactions by userId
        Map<UUID, List<Order>> transactionsGroupedByUser = transactions.stream()
                .collect(Collectors.groupingBy(Order::getUserId));

        logger.info("transaction: {}", transactionsGroupedByUser.size());

        // Calculate totalSpent and successfulTransactions for each user
        return transactionsGroupedByUser.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> (UUID) entry.getKey(), // Explicitly cast the key to UUID
                        entry -> {
                            List<Order> userTransactions = entry.getValue();

                            // Calculate totalSpent (sum of amounts for successful transactions)
                            BigDecimal totalSpent = userTransactions.stream()
                                    .filter(txn -> txn.getStatus() != 5)
                                    .map(Order::getTotalAmount) // Assuming Order has a getTotalAmount method
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            // Calculate successfulTransactions (count of successful transactions)
                            long successfulTransactions = userTransactions.stream()
                                    .count();

                            logger.info("totalSpent: {}", totalSpent);
                            logger.info("transaction: {}", successfulTransactions);

                            // Return a new TransactionResource for this user
                            return TransactionResource.builder()
                                    .totalSpent(totalSpent)
                                    .successfulTransactions(successfulTransactions)
                                    .build();
                        }
                ));
    }

    @Override
    public OrderDetailResource getOrderById(UUID id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        ResponseEntity<?> response = apiClient.getOrderItemProd(order.getId());

        List<OrderItemProdResource> orderItemProdList = objectMapper.convertValue(response.getBody(),
                new TypeReference<List<OrderItemProdResource>>() {

        });

        return OrderDetailResource.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .shippingFee(order.getShippingFee())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItem(new HashSet<>(orderItemProdList))
                .shippingAddresses(order.getShippingAddresses())
                .build();
    }

    @Override
    public Map<UUID, BigDecimal> getTopProductByRevenue(Integer limit) {
        Pageable pageable = PageRequest.of(0, limit);

        List<TopProductDTO> topProductList = orderRepository.findTopProduct(pageable);

        Map<UUID, BigDecimal> result = topProductList.stream()
                .collect(Collectors.toMap(TopProductDTO::getProductId, TopProductDTO::getTotalRevenue));

        return result;
    }

    @Override
    public OrderRequest testRabbitMq(List<OrderRequest> request) {
        rabbitService.sendMessage(myExchange, "test-mq", request);

        return request.get(0);
    }

    @RabbitListener(queues = "test-mq:queue")
    public void testRabbitMqListener(Message message) {
        Optional<List<OrderRequest>> response = rabbitService.deserializeToGeneric(message.getBody(), new TypeReference<List<OrderRequest>>() {
        });

        if (response.isPresent()) {
            logger.info("testRabbitMqListener: {}", response.get().get(0).getOrderCode());
        } else {
            logger.error("Không thể deserialize dữ liệu: {}", new String(message.getBody()));
        }
    }

    @Override
    public String bodySendEmail() {
        String name = "Nguyễn Văn A";
        Order order = orderRepository.findById(UUID.fromString("961d1fcc-1879-4c24-ad99-b85c1f222af0")).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        List<WriteEmailDTO> writeEmailDTOs = List.of(
                WriteEmailDTO.builder()
                        .name("Quần Jeans basic slim")
                        .price(new BigDecimal(399000.00))
                        .imageUrl("v1743237605/20230413_aepumisma9_99b3aae129e34560a3dcacb48d701ffa_master_llxhqs.jpg")
                        .colorName("Đỏ")
                        .sizeName("M")
                        .quantity(2)
                        .build()
        );

        String text = EventUtil.emailBodyForOrder(name, order, imgUrlPrefix, writeEmailDTOs);
        logger.info("text: {}", text);
        return text;
    }
}
