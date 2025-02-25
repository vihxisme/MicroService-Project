package com.service.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.product.entities.Size;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

}
