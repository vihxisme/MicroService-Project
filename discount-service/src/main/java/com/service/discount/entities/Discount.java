package com.service.discount.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Discount {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "discount_code", nullable = false, unique = true)
  private String discountCode;

  @JsonBackReference
  @ManyToOne
  @JoinColumn(name = "type_id", nullable = false)
  private DiscountType discountType;

  @Column(name = "discount_title", nullable = false, columnDefinition = "TEXT")
  private String discountTitle;

  @Column(name = "discount_percentage", nullable = true)
  private BigDecimal discountPercentage;

  @Column(name = "discount_amount", nullable = true)
  private BigDecimal discountAmount;

  @Column(name = "min_order_value", nullable = true)
  private BigDecimal minOrderValue;

  @Column(name = "image_url", nullable = true)
  private String imageUrl;

  @Column(name = "start_date", nullable = false)
  private Timestamp startDate;

  @Column(name = "end_date", nullable = false)
  private Timestamp endDate;

  @Column(name = "is_active", columnDefinition = "Boolean default true")
  private Boolean isActive;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
  private Timestamp createdAt;

  @JsonManagedReference
  @OneToMany(mappedBy = "discount")
  private Set<DiscountTarget> discountTargets;
}
