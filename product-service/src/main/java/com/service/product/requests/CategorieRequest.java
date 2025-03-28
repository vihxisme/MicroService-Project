package com.service.product.requests;

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
public class CategorieRequest {

    private UUID id;
    private String categorieCode;
    private String name;
    private String categorieImageUrl;
    private Integer apparelType;
}
