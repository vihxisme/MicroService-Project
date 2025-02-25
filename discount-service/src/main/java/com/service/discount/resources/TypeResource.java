package com.service.discount.resources;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class TypeResource {
  private final Long id;

  private final String type;
}
