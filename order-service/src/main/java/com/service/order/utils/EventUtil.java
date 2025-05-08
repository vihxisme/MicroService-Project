package com.service.order.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;

import com.service.order.dtos.WriteEmailDTO;
import com.service.order.entities.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventUtil {

    public static String emailBodyForOrder(String name, Order order, String imgUrlPrefix, List<WriteEmailDTO> writeEmailDTOs) {
        StringBuilder productedorder = new StringBuilder();
        int i = 1;
        for (WriteEmailDTO writeEmailDTO : writeEmailDTOs) {
            String imageUrl = String.format("%s%s", imgUrlPrefix, writeEmailDTO.getImageUrl());
            productedorder.append(String.format("""
                    <div>
                        <img src="%s" alt="Hình ảnh %s" style="width: 200px; height: 200px;" />
                        <div>
                            <strong>%d. %s</strong> <br/>
                            Màu sắc: %s <br/>
                            Kích thước: %s <br/>
                            Số lượng: %d <br/>
                            Đơn giá: %s VNĐ <br/>
                            Thành tiền: %s VNĐ <br/>
                        </div>
                    </div>
                    <hr style="border: 1px solid black; margin: 5px 0;"/>
                    """, imageUrl, writeEmailDTO.getName(), i, writeEmailDTO.getName(), writeEmailDTO.getColorName(), writeEmailDTO.getSizeName(), writeEmailDTO.getQuantity(), formatCurrencyVND(writeEmailDTO.getPrice()), formatCurrencyVND(writeEmailDTO.getPrice().multiply(new BigDecimal(writeEmailDTO.getQuantity())))));
            i++;
            log.info("imageUrl", imageUrl);
        }

        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #4CAF50;">Cảm ơn bạn đã đặt hàng tại <strong>BELLION SHOP</strong>!</h2>
                <p>Xin chào <strong>%s</strong>,</p>
                <p>Chúng tôi đã nhận được yêu cầu đặt hàng của bạn và đang trong quá trình xử lý!</p>
                
                <h3>Thông tin đơn hàng:</h3>
                <ul>
                    <li><strong>Mã đơn hàng:</strong> %s</li>
                    <li><strong>Ngày đặt:</strong> %s</li>
                </ul>

                <hr style="border: 1px solid black; margin: 10px 0;"/>

                %s

                <h4>Giá trị đơn hàng: </h4>
                <ul>
                    <li><strong>Tổng tiền:</strong> %s VNĐ</li>
                    <li><strong>Phí vận chuyển:</strong> %s VNĐ</li>
                    <li><strong>Tổng thanh toán:</strong> %s VNĐ</li>
                </ul>

                <p>Chúng tôi sẽ gửi thông báo cho bạn khi đơn hàng được giao cho đơn vị vận chuyển.</p>

                <p>Nếu có bất kỳ thắc mắc nào, hãy liên hệ với chúng tôi qua email hoặc hotline hỗ trợ.</p>

                <p>Trân trọng,<br/>Đội ngũ <strong>BELLION SHOP</strong></p>
                <p>Liên hệ: 0987123456</p>
                <p>Email: bellion.shop@gmail.com</p>
            </body>
            </html>
        """.formatted(name, order.getOrderCode(), order.getCreatedAt(), productedorder.toString(), formatCurrencyVND(order.getTotalAmount().subtract(order.getShippingFee())), formatCurrencyVND(order.getShippingFee()), formatCurrencyVND(order.getTotalAmount()));
        return body;
    }

    public static String emailBodyForCancelOrder(String name, Order order, String imgUrlPrefix, List<WriteEmailDTO> writeEmailDTOs) {
        StringBuilder productedorder = new StringBuilder();
        int i = 1;
        for (WriteEmailDTO writeEmailDTO : writeEmailDTOs) {
            String imageUrl = writeEmailDTO.getImageUrl() == null ? "" : imgUrlPrefix + writeEmailDTO.getImageUrl();
            productedorder.append(String.format("""
                    <div>
                        <img src="%s" alt="Hình ảnh %s" style="width: 50px; height: 50px;" />
                        <div>
                            <strong>%d. %s</strong> <br/>
                            Màu sắc: %s <br/>
                            Kích thước: %s <br/>
                            Số lượng: %d <br/>
                            Đơn giá: %s VNĐ <br/>
                            Thành tiền: %s VNĐ <br/>
                        </div>
                    </div>
                    <hr style="border: 1px solid black; margin: 5px 0;"/>
                    """, imageUrl, writeEmailDTO.getName(), i, writeEmailDTO.getName(), writeEmailDTO.getColorName(), writeEmailDTO.getSizeName(), writeEmailDTO.getQuantity(), formatCurrencyVND(writeEmailDTO.getPrice()), formatCurrencyVND(writeEmailDTO.getPrice().multiply(new BigDecimal(writeEmailDTO.getQuantity())))));
            i++;
        }

        String body = """
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #4CAF50;">Cảm ơn bạn đã đặt hàng tại <strong>BELLION SHOP</strong>!</h2>
                <p>Xin chào <strong>%s</strong>,</p>
                <p>Chúng tôi rất tiếc phải thông báo rằng đơn hàng của bạn đã bị hủy do một số sản phẩm trong đơn hàng hiện đã hết hàng.</p>
                
                <h3>Thông tin đơn hàng:</h3>
                <ul>
                    <li><strong>Mã đơn hàng:</strong> %s</li>
                    <li><strong>Ngày đặt:</strong> %s</li>
                </ul>

                <hr style="border: 1px solid black; margin: 10px 0;"/>

                %s

                <h4>Giá trị đơn hàng: </h4>
                <ul>
                    <li><strong>Tổng tiền:</strong> %s VNĐ</li>
                    <li><strong>Phí vận chuyển:</strong> %s VNĐ</li>
                    <li><strong>Tổng thanh toán:</strong> %s VNĐ</li>
                </ul>

                <p>Chúng tôi thành thật xin lỗi vì sự bất tiện này.</p>

                <p>Nếu bạn cần hỗ trợ thêm, vui lòng liên hệ với chúng tôi qua email hoặc hotline hỗ trợ.</p>

                <p>Trân trọng,<br/>Đội ngũ <strong>BELLION SHOP</strong></p>
                <p>Liên hệ: 0987123456</p>
                <p>Email: bellion.shop@gmail.com</p>
            </body>
            </html>
        """.formatted(name, order.getOrderCode(), order.getCreatedAt(), productedorder.toString(), formatCurrencyVND(order.getTotalAmount().subtract(order.getShippingFee())), formatCurrencyVND(order.getShippingFee()), formatCurrencyVND(order.getTotalAmount()));
        return body;
    }

    public static String formatCurrencyVND(BigDecimal amount) {
        Locale localeVN = Locale.of("vi", "VN");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(localeVN);
        return currencyFormatter.format(amount);
    }
}
