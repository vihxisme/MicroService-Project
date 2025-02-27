package com.service.apicomposition.mappers;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.service.apicomposition.resources.DiscountClientResource;
import com.service.apicomposition.resources.ProductClientResource;

import jakarta.inject.Named;

@Component
public class ComponentMapper {

  private Logger logger = LoggerFactory.getLogger(ComponentMapper.class);

  @Named("calculateFinalPrice")
  public BigDecimal calculateFinalPrice(ProductClientResource product, DiscountClientResource discount) {
    if (discount == null) {
      return product.getPrice(); // Không có giảm giá
    }

    // 🛠 Kiểm tra xem giảm giá có áp dụng được không
    boolean isApplicable = discount.getTargetId() == null ||
        (discount.getTargetType().toUpperCase().equals("PRODUCT") && discount.getTargetId().equals(product.getId())) ||
        (discount.getTargetType().toUpperCase().equals("CATEGORIE")
            && discount.getTargetId().equals(product.getCategorieId()));

    if (!isApplicable) {
      return product.getPrice();
    }

    // 🛠 Kiểm tra minOrderValue (nếu có)
    if (discount.getMinOrderValue() != null && product.getPrice().compareTo(discount.getMinOrderValue()) < 0) {
      return product.getPrice(); // Không đủ điều kiện để nhận giảm giá
    }

    // 🛠 Áp dụng giảm giá theo phần trăm hoặc số tiền cụ thể
    BigDecimal finalPrice = product.getPrice();

    if (discount.getDiscountPercentage() != null && discount.getDiscountPercentage().compareTo(BigDecimal.ZERO) > 0) {
      finalPrice = finalPrice.subtract(finalPrice.multiply(discount.getDiscountPercentage())
          .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
    } else if (discount.getDiscountAmount() != null && discount.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
      finalPrice = finalPrice.subtract(discount.getDiscountAmount());
    }

    return finalPrice.max(BigDecimal.ZERO); // Không cho giá âm
  }

}
