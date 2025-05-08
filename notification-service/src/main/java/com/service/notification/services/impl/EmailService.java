package com.service.notification.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.service.events.NotificationEvent;
import com.service.notification.exceptions.ErrorCode;
import com.service.notification.exceptions.SendEmailException;
import com.service.notification.repositories.ApiClient;
import com.service.notification.requests.EmailRequest;
import com.service.notification.requests.RecipientRequest;
import com.service.notification.requests.SendEmailRequest;
import com.service.notification.requests.SenderRequest;
import com.service.notification.responses.EmailResponse;
import com.service.notification.services.interfaces.EmailInterface;

import feign.FeignException;

@Service
public class EmailService implements EmailInterface {

    @Autowired
    private ApiClient apiClient;

    @Autowired
    private RabbitService rabbitService;

    @Value("${notification.email.brevo-apikey}")
    private String apiKey;

    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Override
    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(SenderRequest.builder()
                        .name("BELLION SHOP")
                        .email("nguyenvanvinh9203@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();

        try {
            return apiClient.sendEmail(apiKey, emailRequest);
        } catch (FeignException e) {
            throw new SendEmailException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }

    @RabbitListener(queues = "send-email:queue")
    public void sendEmailListener(Message message) {
        Optional<NotificationEvent> notificationEvent = rabbitService.deserializeToObject(message.getBody(), NotificationEvent.class);

        if (notificationEvent.isPresent()) {
            SendEmailRequest request = SendEmailRequest.builder()
                    .to(RecipientRequest.builder()
                            .email(notificationEvent.get().getEmail())
                            .name(notificationEvent.get().getName())
                            .build())
                    .subject(notificationEvent.get().getSubject())
                    .htmlContent(notificationEvent.get().getBody())
                    .build();
            sendEmail(request);
        }
    }

}
