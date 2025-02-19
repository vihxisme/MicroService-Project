package com.service.customer.entities;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.service.customer.enums.GenderEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "customer_code", unique = true, nullable = false)
  private String customerCode;

  @JdbcTypeCode(SqlTypes.BINARY)
  @Column(name = "auth_user_id", unique = true, nullable = false, columnDefinition = "BINARY(16)")
  private UUID authUserId;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Enumerated(EnumType.STRING)
  @Column(name = "gender", columnDefinition = "ENUM('Male', 'Female', 'other')")
  private GenderEnum gender;

  @Temporal(TemporalType.DATE)
  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "phone", unique = true, nullable = false, length = 15)
  private String phone;

  @Lob
  @Column(name = "avatar", columnDefinition = "MEDIUMBLOB")
  private byte[] avatar;

  @CreationTimestamp
  @Column(name = "created_at")
  private Timestamp createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @JsonManagedReference
  @OneToMany(mappedBy = "customerID", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Address> addresses;
}
