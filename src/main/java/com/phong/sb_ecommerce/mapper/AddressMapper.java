package com.phong.sb_ecommerce.mapper;

import com.phong.sb_ecommerce.model.Address;
import com.phong.sb_ecommerce.payload.dto.AddressDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toAddressDTO(Address address);
    Address toAddress(AddressDTO addressDTO);
}
