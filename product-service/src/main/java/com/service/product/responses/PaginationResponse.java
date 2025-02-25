package com.service.product.responses;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class PaginationResponse<T> {
  private final List<T> content;
  private final int pageNumber;
  private final int pageSize;
  private final int totalPages;
  private final long totalElements;
}
