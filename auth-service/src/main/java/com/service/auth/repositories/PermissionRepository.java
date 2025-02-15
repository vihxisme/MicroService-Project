package com.service.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.auth.entities.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
  int findIdByName(String name);
}
