package com.service.order.entities;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "shipping_addresses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order orders;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
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

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private Timestamp createdAt;
}
