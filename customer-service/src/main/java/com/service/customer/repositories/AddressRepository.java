package com.service.customer.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.service.customer.entities.Address;
import com.service.customer.entities.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  List<Address> findByCustomerID(Customer customer);

  @Modifying
  @Query("""
      DELETE FROM Address a WHERE a.id=:id
      """)
  int deleteByIdCustom(@Param("id") Long id);

  @Modifying
  @Query("""
      UPDATE Address a SET a.isDefault=false WHERE a.customerID=:customerId
      """)
  void updateIsDefaultByCustomerID(@Param("customerId") Customer customerId);
}
