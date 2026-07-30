package io.github.gabrielhe4.shop_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.github.gabrielhe4.shop_api.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    Cart findCartByEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = :email AND c.id = :cartId")
    Cart findCartByEmailAndCartId(String email, Long cartId);

    @Query("""
            SELECT c FROM Cart c
            JOIN FETCH c.cartItems ci
            JOIN FETCH ci.product p
            WHERE p = :productId
            """)
    List<Cart> findCartsByProductId(Long productId);

}
