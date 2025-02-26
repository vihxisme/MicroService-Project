package com.service.discount.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class TypeResource {
  private final Integer id;

  private final String type;
}
