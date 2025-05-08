package com.service.notification.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.notification.requests.SendEmailRequest;
import com.service.notification.responses.SuccessResponse;
import com.service.notification.services.interfaces.EmailInterface;

@RestController
@RequestMapping("/v1/email")
public class EmailController {

    @Autowired
    private EmailInterface emailInterface;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody SendEmailRequest request) {
        return ResponseEntity.ok(
                new SuccessResponse<>(
                        "SUCCESS",
                        emailInterface.sendEmail(request))
        );
    }
}
