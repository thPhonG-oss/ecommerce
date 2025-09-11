package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.model.Cart;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import jakarta.transaction.Transactional;

public interface CartService {

    ApiResponse<?> addProductToCart(Long productId, Integer quantity);

    @Transactional
    Cart createCartUser();

    ApiResponse<?> findAllCarts();

    ApiResponse<?> getCartOfTheSpecificUserEmail();

    ApiResponse<?> updateProductQuantityInCart(Long productId, Integer quantity);

    ApiResponse<?> deleteItemInCart(Long cartId, Long productId);

//    ApiResponse<?> getCartByEmailAndCartId();
}
