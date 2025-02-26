package com.service.discount.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "discount_types")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DiscountType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "type_code", unique = true, nullable = false)
  private String typeCode;

  @Column(name = "type", unique = true, nullable = false)
  private String type;

  @JsonManagedReference
  @OneToMany(mappedBy = "discountType")
  private Set<Discount> discounts;

  @PrePersist
  @PreUpdate
  private void convertTypeToUpperCase() {
    this.type = this.type.toUpperCase();
  }
}
