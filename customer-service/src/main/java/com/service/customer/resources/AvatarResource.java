package com.service.customer.resources;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class AvatarResource {
  private final Boolean isAvatar;
}
