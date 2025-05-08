package com.service.notification.services.interfaces;

import com.service.notification.requests.SendEmailRequest;
import com.service.notification.responses.EmailResponse;

public interface EmailInterface {

    EmailResponse sendEmail(SendEmailRequest request);
}
