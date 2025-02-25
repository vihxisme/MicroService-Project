package com.service.discount.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PaginationResponse<T> {
  private final List<T> content;
  private final int pageNumber;
  private final int pageSize;
  private final int totalPages;
  private final long totalElements;
}
