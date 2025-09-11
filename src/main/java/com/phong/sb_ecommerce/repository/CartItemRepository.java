package com.phong.sb_ecommerce.repository;

import com.phong.sb_ecommerce.model.CartItem;
import com.phong.sb_ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(
        value = "SELECT ci FROM CartItem ci JOIN Cart c ON ci.cart.cartId=:cartId JOIN Product p ON ci.product.productId=:productId"
    )
    CartItem findCartItemByProductIdAndCartId(@Param("cartId") Long cartId,@Param("productId") Long productId);
}
