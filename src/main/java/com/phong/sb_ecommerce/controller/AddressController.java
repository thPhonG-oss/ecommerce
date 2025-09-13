package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.dto.AddressDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody @Valid AddressDTO addressDTO) {
        return new ResponseEntity<AddressDTO>(addressService.createAddress(addressDTO), HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddressesInSytem() {
        return ResponseEntity.ok().body(addressService.getAllAddresses());
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        return ResponseEntity.ok().body(addressService.getAddressById(addressId));
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesBySpecificUser(){
        return ResponseEntity.ok().body(addressService.getUserAddresses());
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId , @RequestBody @Valid AddressDTO addressDTO) {
        return new ResponseEntity<>(addressService.updateAddress(addressId, addressDTO), HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<ApiResponse<?>> deleteAddress(@PathVariable Long addressId) {
        return new ResponseEntity<ApiResponse<?>>(ApiResponse.builder()
            .status("SUCCESS")
            .message(addressService.deleteAddress(addressId))
            .build(),
            HttpStatus.OK);
    }
}
