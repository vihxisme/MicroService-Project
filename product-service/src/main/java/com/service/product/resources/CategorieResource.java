package com.service.product.resources;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class CategorieResource {
  private final UUID id;
  private final String categorieCode;
  private final String name;
  private final String categorieImageUrl;
}
