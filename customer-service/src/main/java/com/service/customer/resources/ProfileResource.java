package com.service.customer.resources;

import java.sql.Date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ProfileResource {

    private final String firstName;
    private final String lastName;
    private final String gender;
    private final Date dateOfBirth;
    private final String email;
    private final String phone;
    private final String avatar;
    private final String address;
    private final Integer ward;
    private final Integer district;
    private final Integer province;
}
