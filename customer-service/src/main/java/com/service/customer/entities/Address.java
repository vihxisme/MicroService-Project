package com.service.customer.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customerID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "ward", nullable = false)
    private Integer ward;

    @Column(name = "district", nullable = false)
    private Integer district;

    @Column(name = "province", nullable = false)
    private Integer province;

    @Column(name = "full_address")
    private String fullAddress;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_default", columnDefinition = "Boolean default false")
    private Boolean isDefault;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
