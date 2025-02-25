package com.service.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.product.entities.Color;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

}
