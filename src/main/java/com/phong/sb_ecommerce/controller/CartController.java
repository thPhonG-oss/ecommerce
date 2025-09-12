package com.phong.sb_ecommerce.controller;

import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.service.CartService;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<ApiResponse<?>> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        return new ResponseEntity<>(
            ApiResponse.builder()
            .status(HttpStatus.CREATED.toString())
            .message("Successfully added product to the cart")
            .response(cartService.addProductToCart(productId, quantity))
            .build(),
            HttpStatus.CREATED
        );
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<?> getAllCarts() {
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all carts")
                .response(cartService.findAllCarts())
                .build()
            ,
            HttpStatus.OK
        );
    }

    // get cart of the specific user
    @GetMapping("/carts/users/cart")
    public ResponseEntity<?> getCartById() {
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all carts")
                .response(cartService.getCartOfTheSpecificUserEmail())
                .build()
            ,
            HttpStatus.OK
        );
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<?> updateProductQuantity(@PathVariable Long productId, @PathVariable String operation) { // or updateCartProduct
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all carts")
                .response(cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete")?-1:1))
                .build()
            ,
            HttpStatus.OK
        );
    }

    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<?> deleteProductInCart(
        @PathVariable Long cartId,
        @PathVariable Long productId)
    {
        return new ResponseEntity<>(
            ApiResponse.builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all carts")
                .response(cartService.deleteItemInCart(cartId, productId))
                .build()
            ,
            HttpStatus.OK
        );
    }
}
