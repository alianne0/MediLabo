package com.medilabo.demographics.repository;

import com.medilabo.demographics.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<Users, Long>{
    Users findByUsername(String username);
    boolean existsByUsername(String username);
}
