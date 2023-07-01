package com.bbm.employeeservice.service;

import com.bbm.employeeservice.exception.EntityNotFoundException;
import com.bbm.employeeservice.model.Address;
import com.bbm.employeeservice.model.dto.AddressResponse;
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

    public AddressResponse getAddress(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Endereco com ID: " + id + "não foi encontrado"));
        return mapToAddressResponse(address);
    }

    public AddressResponse mapToAddressResponse(Address address) {
        return AddressResponse.builder()
                .houseNumber(address.getHouseNumber())
                .street(address.getStreet())
                .zipCode(address.getZipCode())
                .build();
    }
}