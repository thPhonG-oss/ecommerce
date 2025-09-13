package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.exception.APIException;
import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.mapper.AddressMapper;
import com.phong.sb_ecommerce.model.Address;
import com.phong.sb_ecommerce.model.User;
import com.phong.sb_ecommerce.payload.dto.AddressDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.repository.AddressRepository;
import com.phong.sb_ecommerce.repository.UserRepository;
import com.phong.sb_ecommerce.service.AddressService;
import com.phong.sb_ecommerce.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AuthUtils authUtils;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO){
        Address newAddress = addressMapper.toAddress(addressDTO);
        User user = authUtils.loggedInUser();

        List<Address> addresses = user.getAddresses();

        addresses.forEach(address1 -> {
            if(address1.equals(newAddress)){
                throw new APIException("This address is already in use");
            }
        });

        newAddress.setUser(user);
        Address savedAddress = addressRepository.save(newAddress);

        addresses.add(newAddress);

        user.setAddresses(addresses);
        userRepository.save(user);

        return addressMapper.toAddressDTO(savedAddress);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // hasAuthority check both role and permission, therefore we need type all prefix 'ROLE_'.
    @Override
    public List<AddressDTO> getAllAddresses(){

        List<Address> addresses = addressRepository.findAll();
        if(addresses.isEmpty()){
            throw new APIException("No addresses found");
        }

        List<AddressDTO> addressDTOS = addressRepository.findAll()
            .stream().map(addressMapper::toAddressDTO).toList();

        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId){
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourcesNotFoundException("Address", "id", addressId));

        return addressMapper.toAddressDTO(address);
    }

    @Override
    public List<AddressDTO> getUserAddresses(){
        User user = authUtils.loggedInUser();
        List<Address> addresses = user.getAddresses();

        if(addresses.isEmpty()){
            throw new APIException("No addresses found");
        }

        return addresses.stream().map(addressMapper::toAddressDTO).toList();
    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO){
        Address existingAddress = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourcesNotFoundException("Address", "id", addressId));

        existingAddress.setBuildingName(addressDTO.getBuildingName());
        existingAddress.setStreet(addressDTO.getStreet());
        existingAddress.setCity(addressDTO.getCity());
        existingAddress.setCountry(addressDTO.getCountry());
        existingAddress.setPincode(addressDTO.getPincode());

        Address updatedAddress = addressRepository.save(existingAddress);

        User user = updatedAddress.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(updatedAddress.getAddressId()));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);

        return addressMapper.toAddressDTO(updatedAddress);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourcesNotFoundException("Address", "id", addressId));

        User user = address.getUser();
        user.getAddresses().remove(address);

        userRepository.save(user);

        addressRepository.delete(address);

        return "Delete address successfully";
    }


}
