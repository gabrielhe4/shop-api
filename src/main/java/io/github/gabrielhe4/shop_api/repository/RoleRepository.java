package io.github.gabrielhe4.shop_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.enumeration.AppRole;
import io.github.gabrielhe4.shop_api.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByRoleName(AppRole roleUser);

}
