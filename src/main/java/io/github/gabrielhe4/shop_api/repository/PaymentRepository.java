package io.github.gabrielhe4.shop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.gabrielhe4.shop_api.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
