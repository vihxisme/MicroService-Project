package com.service.product.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.service.product.enums.StatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "product_code", nullable = false, unique = true)
  private String productCode;

  @JsonBackReference
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id")
  private Categorie categorie;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private BigDecimal price;

  @Column(name = "brand", nullable = true)
  private String brand;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", columnDefinition = "ENUM('ACTIVE', 'INACTIVE', 'OUT_OF_STOCK') DEFAULT 'INACTIVE'")
  private StatusEnum status;

  @Column(name = "product_image_url", nullable = false)
  private String productImageUrl;

  @CreationTimestamp
  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = false)
  private Timestamp createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, updatable = true)
  private Timestamp updatedAt;

  @JsonManagedReference
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ProductVariant> productVariants;

  @JsonManagedReference
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ProductImage> productImages;

  @JsonManagedReference
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ProductDetail> productDetails;
}
