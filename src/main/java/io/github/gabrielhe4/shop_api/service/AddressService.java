package io.github.gabrielhe4.shop_api.service;

import java.util.List;

import io.github.gabrielhe4.shop_api.dto.AddressResponse;
import io.github.gabrielhe4.shop_api.dto.AddressRequest;
import io.github.gabrielhe4.shop_api.model.User;

public interface AddressService {

    AddressResponse create(AddressRequest request, User user);

    List<AddressResponse> getAllAddresses();

    AddressResponse getAddressById(Long id);

    List<AddressResponse> getAddressesByUser(User user);

    AddressResponse updateAddress(Long id, AddressRequest request);

    void deleteAddress(Long addressId);

}
