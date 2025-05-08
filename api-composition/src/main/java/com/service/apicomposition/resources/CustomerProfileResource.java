package com.service.apicomposition.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class CustomerProfileResource {

    private final UUID id;
    private final String customerCode;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final String email;
    private final String phone;
    private final String avatar;
}
