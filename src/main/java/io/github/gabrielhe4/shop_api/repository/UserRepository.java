package io.github.gabrielhe4.shop_api.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.gabrielhe4.shop_api.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
