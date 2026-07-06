package io.github.gabrielhe4.shop_api.service;

import java.util.List;

import io.github.gabrielhe4.shop_api.dto.AddressDTO;
import io.github.gabrielhe4.shop_api.dto.AddressRequest;
import io.github.gabrielhe4.shop_api.model.User;

public interface AddressService {

    AddressDTO create(AddressRequest request, User user);

    List<AddressDTO> getAllAddresses();

    AddressDTO getAddressById(Long id);

    List<AddressDTO> getAddressesByUser(User user);

    AddressDTO updateAddress(Long id, AddressRequest request);

    void deleteAddress(Long addressId);

}
