package com.service.customer.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.customer.dtos.StatsNewCustomerDTO;
import com.service.customer.entities.Customer;
import com.service.customer.resources.CustomerProfileResource;
import com.service.customer.resources.ProfileResource;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Boolean existsByCustomerCode(String customerCode);

    Optional<Customer> findByAuthUserId(UUID authUserId);

    @Modifying
    @Query("""
      UPDATE Customer c SET c.avatar=:avatar WHERE c.id=:id
      """)
    int updateAvatar(@Param("id") UUID id, @Param("avatar") String avatar);

    @Query("SELECT c.id FROM Customer c WHERE c.authUserId = :authUserId")
    Optional<UUID> findIdByAuthUserId(@Param("authUserId") UUID authUserId);

    @Override
    Page<Customer> findAll(Pageable pageable);

    @Query("""
      SELECT new com.service.customer.resources.ProfileResource(
        c.firstName,
        c.lastName,
        CAST(c.gender AS String),
        CAST(c.dateOfBirth AS Date),
        c.email,
        c.phone,
        c.avatar,
        a.address,
        a.ward,
        a.district,
        a.province
      )
      FROM Customer c
      LEFT JOIN c.addresses a ON a.isDefault = true
      WHERE c.id = :id
      """)
    ProfileResource findProfileById(@Param("id") UUID id);

    @Query("""
        SELECT COUNT(c)
        FROM Customer c
        """)
    Long countCustomer();

    @Query(value = """
        SELECT COUNT(*) AS totalCustomers
        FROM customers c
        WHERE created_at BETWEEN :start AND :end
    """, nativeQuery = true)
    StatsNewCustomerDTO countNewCustomerBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
        SELECT new com.service.customer.resources.CustomerProfileResource(
          c.id,
          c.customerCode,
          c.firstName,
          c.lastName,
          CAST(c.gender AS String),
          c.email,
          c.phone,
          c.avatar
        )
        FROM Customer c
        WHERE c.createdAt BETWEEN :start AND :end
        """)
    List<CustomerProfileResource> findNewCustomerByRangeType(
            Pageable pageable,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
