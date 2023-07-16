package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByOriginalFileName(String fileName);
}
