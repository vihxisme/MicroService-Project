package com.service.order.dtos;

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
public class OrderStatusCountImplDTO implements OrderStatusCountInterfaceDTO {

    private Integer status;
    private Long count;
}
