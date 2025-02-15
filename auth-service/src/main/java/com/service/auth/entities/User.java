package com.service.auth.entities;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.service.auth.enums.RoleEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.BINARY)
  @Column(name = "id", columnDefinition = "BINARY(16)")
  private UUID id;

  @Column(name = "email", unique = true, nullable = false, length = 100)
  private String email;

  @Column(name = "username", unique = true, nullable = false, length = 50)
  private String username;

  @Lob
  @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
  private String password;

  // @ManyToMany
  // @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
  // inverseJoinColumns = @JoinColumn(name = "role_id"))
  // private Set<Role> roles;
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false, columnDefinition = "ENUM('ADMIN', 'CUSTOMER', 'SELLER', 'GUEST', 'SUPPORT_AGENT', 'MANAGER', 'DELIVERY_AGENT', 'FINANCE', 'INVENTORY_MANAGER', 'MARKETING', 'REVIEWER', 'CONTENT_MANAGER', 'ANALYST')")
  private RoleEnum role = RoleEnum.CUSTOMER;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false, insertable = false)
  private Timestamp createdAt;

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }
}