package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    @Query(nativeQuery = true, value = "select count(*) from users where is_enabled = true")
    Integer countAllEnabledUser();
    boolean existsByEmail(String email);
}
