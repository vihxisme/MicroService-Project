package com.service.about.requests;

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
public class AboutStoreRequest {

    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String logo;
    private String content;
}
