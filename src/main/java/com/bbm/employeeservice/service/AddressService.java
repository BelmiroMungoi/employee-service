package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Address;
import com.bbm.employeeservice.model.dto.EmployeeRequest;
import com.bbm.employeeservice.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;


    public Address saveAddress(EmployeeRequest employeeRequest) {
        Address address = Address.builder()
                .houseNumber(employeeRequest.getHouseNumber())
                .street(employeeRequest.getStreet())
                .zipCode(employeeRequest.getZipCode())
                .build();

        return addressRepository.save(address);
    }

    public Address getAddress(Long id) {
        return addressRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Endereco com ID: " + id + "não foi encontrado"));
    }

    public Address updateAddress(Long id, EmployeeRequest request) {
        Address address = getAddress(id);
        address.setHouseNumber(request.getHouseNumber());
        address.setStreet(request.getStreet());
        address.setZipCode(request.getZipCode());

        return addressRepository.save(address);
    }

}