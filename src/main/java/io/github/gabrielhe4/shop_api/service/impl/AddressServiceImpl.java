package io.github.gabrielhe4.shop_api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.AddressDTO;
import io.github.gabrielhe4.shop_api.dto.AddressRequest;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.AddressMapper;
import io.github.gabrielhe4.shop_api.model.Address;
import io.github.gabrielhe4.shop_api.model.User;
import io.github.gabrielhe4.shop_api.repository.AddressRepository;
import io.github.gabrielhe4.shop_api.repository.UserRepository;
import io.github.gabrielhe4.shop_api.service.AddressService;
import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public AddressDTO create(AddressRequest request, User user) {
        Address newAddress = AddressRequest.toAddress(request);
        newAddress.setUser(user);
        Address savedAddress = addressRepository.save(newAddress);

        List<Address> userAddresses = user.getAddresses();
        userAddresses.add(savedAddress);
        user.setAddresses(userAddresses);
        userRepository.save(user);

        return AddressMapper.INSTANCE.toDTO(savedAddress);

    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();

        return addresses.stream()
                .map(AddressMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Address", "id", id));
        
        return AddressMapper.INSTANCE.toDTO(address);
    }

    @Override
    public List<AddressDTO> getAddressesByUser(User user) {
        List<Address> addresses = user.getAddresses();

        return addresses.stream()
                .map(AddressMapper.INSTANCE::toDTO)
                .toList();
    }

    @Override
    public AddressDTO updateAddress(Long id, AddressRequest request) {
        Address existingAddress = addressRepository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Address", "id", id)
        );
        existingAddress.setStreet(request.street());
        existingAddress.setBuildingName(request.buildingName());
        existingAddress.setCity(request.city());
        existingAddress.setState(request.state());
        existingAddress.setCountry(request.country());
        existingAddress.setZipcode(request.zipcode());

        Address updatedAddress = addressRepository.save(existingAddress);

        User user = existingAddress.getUser();
        user.getAddresses().removeIf(a -> a.getId().equals(id));
        user.getAddresses().add(updatedAddress);

        userRepository.save(user);

        return AddressMapper.INSTANCE.toDTO(updatedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        Address existingAddress = addressRepository.findById(addressId).orElseThrow(
            () -> new ResourceNotFoundException("Address", "id", addressId)
        );
        
        User user = existingAddress.getUser();
        user.getAddresses().removeIf(a -> a.getId().equals(addressId));
        userRepository.save(user);

        addressRepository.delete(existingAddress);
    }


}
