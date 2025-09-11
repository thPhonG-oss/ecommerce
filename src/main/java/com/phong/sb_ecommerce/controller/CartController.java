package com.phong.sb_ecommerce.controller;

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
    public ResponseEntity<?> addProductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
        return new ResponseEntity<>(cartService.addProductToCart(productId, quantity), HttpStatus.CREATED);
    }

    @GetMapping("/admin/carts")
    public ResponseEntity<?> getAllCarts() {
        return new ResponseEntity<>(cartService.findAllCarts(), HttpStatus.OK);
    }

    // get cart of the specific user
    @GetMapping("/carts/users/cart")
    public ResponseEntity<?> getCartById() {
        return new ResponseEntity<>(cartService.getCartOfTheSpecificUserEmail(), HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<?> updateProductQuantity(@PathVariable Long productId, @PathVariable String operation) { // or updateCartProduct
        return new ResponseEntity<>(cartService.updateProductQuantityInCart(productId, operation.equalsIgnoreCase("delete")?-1:1), HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<?> deleteProductInCart(
        @PathVariable Long cartId,
        @PathVariable Long productId)
    {

        return new ResponseEntity<>(cartService.deleteItemInCart(cartId, productId), HttpStatus.OK);
    }
}
