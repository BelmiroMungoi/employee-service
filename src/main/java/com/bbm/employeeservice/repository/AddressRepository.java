package com.bbm.employeeservice.repository;

import com.bbm.employeeservice.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
