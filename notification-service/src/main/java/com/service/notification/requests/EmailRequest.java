package com.service.notification.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailRequest {

    private SenderRequest sender;
    private List<RecipientRequest> to;
    private String subject;
    private String htmlContent;
}
