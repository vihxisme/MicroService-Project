package com.service.product.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class SizeResource {
  private final Integer id;
  private final String name;
}
