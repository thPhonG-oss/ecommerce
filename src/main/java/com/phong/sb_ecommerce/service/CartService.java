package com.phong.sb_ecommerce.service;

import com.phong.sb_ecommerce.model.Cart;
import com.phong.sb_ecommerce.model.CartItem;
import com.phong.sb_ecommerce.payload.dto.CartDTO;
import com.phong.sb_ecommerce.payload.dto.CartItemDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(Long productId, Integer quantity);

    @Transactional
    Cart createCartUser();

    List<CartDTO> findAllCarts();

    CartDTO getCartOfTheSpecificUserEmail();

    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    CartDTO deleteItemInCart(Long cartId, Long productId);

    CartItemDTO updateItemInCart(Long cartId, Long productId);

//    ApiResponse<?> getCartByEmailAndCartId();
}
