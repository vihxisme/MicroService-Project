package com.service.customer.mappers;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.service.customer.enums.GenderEnum;

@Component
public class GenderConverter {
  @Named("fromString")
  public GenderEnum fromString(String gender) {
    return GenderEnum.valueOf(gender);
  }

  @Named("toString")
  public String toString(GenderEnum gender) {
    return gender.name();
  }
}
