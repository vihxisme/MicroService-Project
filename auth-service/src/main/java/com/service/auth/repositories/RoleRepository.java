package com.service.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.auth.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Role findByName(String name);
}
