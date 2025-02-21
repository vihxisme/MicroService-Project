package com.service.customer.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.customer.entities.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
  Boolean existsByCustomerCode(String customerCode);

  Optional<Customer> findByAuthUserId(UUID authUserId);

  @Modifying
  @Query("""
      UPDATE Customer c SET c.avatar=:avatar WHERE c.id=:id
      """)
  int updateAvatar(@Param("id") UUID id, @Param("avatar") byte[] avatar);

  @Query("SELECT c.id FROM Customer c WHERE c.authUserId = :authUserId")
  Optional<UUID> findIdByAuthUserId(@Param("authUserId") UUID authUserId);

  @Override
  Page<Customer> findAll(Pageable pageable);
}
