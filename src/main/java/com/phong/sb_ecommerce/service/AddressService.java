package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.payload.dto.AddressDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddresses();

//    AddressDTO updateAddress(AddressDTO addressDTO);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
