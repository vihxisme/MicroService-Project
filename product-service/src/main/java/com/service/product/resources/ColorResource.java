package com.service.product.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class ColorResource {
  private final Integer id;
  private final String name;
}
