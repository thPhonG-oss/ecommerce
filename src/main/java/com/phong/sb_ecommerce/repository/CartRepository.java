package com.phong.sb_ecommerce.repository;

import com.phong.sb_ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c  WHERE c.cartUser.email=:email")
    Cart findCartByEmail(@Param("email") String email);

    @Query("SELECT c FROM Cart c WHERE c.cartUser.email=?1")
    Optional<Cart> findCartByUseremail(String email);

    @Query(
        value = "SELECT c FROM Cart c WHERE c.cartUser.email=:email AND c.cartId=:cartId"
    )
    Cart findCartByEmailAndCartId(@Param("email") String emailId,@Param("cartId") Long cartId);

    @Query(value = "SELECT c FROM Cart  c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.productId=?1")
    List<Cart> findCartByProductId(Long productId);
}
