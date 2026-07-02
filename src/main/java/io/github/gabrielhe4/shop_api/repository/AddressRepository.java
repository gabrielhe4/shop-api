package io.github.gabrielhe4.shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
