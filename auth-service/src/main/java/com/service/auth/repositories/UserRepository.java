package com.service.auth.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.auth.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  User findByUsername(String username);

  User findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String username, String email);
}
