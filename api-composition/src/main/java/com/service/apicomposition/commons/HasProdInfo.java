package com.service.apicomposition.commons;

import java.math.BigDecimal;
import java.util.UUID;

public interface HasProdInfo {

    BigDecimal getPrice();

    UUID getId();

    UUID getCategorieId();
}
