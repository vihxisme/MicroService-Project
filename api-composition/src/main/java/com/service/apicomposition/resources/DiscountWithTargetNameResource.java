package com.service.apicomposition.resources;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class DiscountWithTargetNameResource {

    @JsonProperty("idTarget")
    private Integer idTarget;

    @JsonProperty("discountId")
    private UUID discountId;

    @JsonProperty("discountCode")
    private String discountCode;

    @JsonProperty("discountTitle")
    private String discountTitle;

    @JsonProperty("discountPercentage")
    private BigDecimal discountPercentage;

    @JsonProperty("discountAmount")
    private BigDecimal discountAmount;

    @JsonProperty("minOrderValue")
    private BigDecimal minOrderValue;

    @JsonProperty("targetType")
    private String targetType;

    @JsonProperty("targetId")
    private UUID targetId;

    @JsonProperty("targetName")
    private String targetName;
}
