package com.service.discount.requests;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TargetRequest {
  private Long id;
  private UUID discountId;
  private String targetType;
  private UUID targetId;
}
