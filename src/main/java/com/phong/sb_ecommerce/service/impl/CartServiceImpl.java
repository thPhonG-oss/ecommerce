package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.exception.APIException;
import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.mapper.CartItemMapper;
import com.phong.sb_ecommerce.mapper.CartMapperImpl;
import com.phong.sb_ecommerce.model.Cart;
import com.phong.sb_ecommerce.model.CartItem;
import com.phong.sb_ecommerce.model.Product;
import com.phong.sb_ecommerce.payload.dto.CartDTO;
import com.phong.sb_ecommerce.payload.dto.CartItemDTO;
import com.phong.sb_ecommerce.payload.response.ApiResponse;
import com.phong.sb_ecommerce.repository.CartItemRepository;
import com.phong.sb_ecommerce.repository.CartRepository;
import com.phong.sb_ecommerce.repository.ProductRepository;
import com.phong.sb_ecommerce.service.CartService;
import com.phong.sb_ecommerce.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    private CartMapperImpl cartMapper;

    @Autowired
    private CartItemMapper cartItemMapper;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity){
        // first, find existing cart of current user or creating one.
        Cart cart = createCartUser();
        //  retrieve product details
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourcesNotFoundException("Product", "id", productId));

        // perform validations
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(), productId);

        if(cartItem != null){
            throw new APIException(product.getProductName() + " already exists in your Cart");
        }

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available.");
        }

        if(quantity > product.getQuantity()){
            throw new APIException("Please make order of the " + product.getProductName() + " less than or equal to " + product.getQuantity() + ".");
        }
        // create cart item
        CartItem cartItemToAdd = new CartItem();
        cartItemToAdd.setCart(cart);
        cartItemToAdd.setProduct(product);
        cartItemToAdd.setQuantity(quantity);
        cartItemToAdd.setDiscount(product.getDiscount());
        cartItemToAdd.setProductPrice(product.getSpecialPrice());
        // save cart item
        CartItem savedCartItem = cartItemRepository.save(cartItemToAdd);
        // return updated cart information
        cart.getCartItems().add(savedCartItem);
        cart.setTotalPrice(cart.getTotalPrice() + (cartItemToAdd.getProductPrice() * quantity));
        cartRepository.save(cart);
        return cartMapper.toCartDTO(cart);
    }

    @Override
    public Cart createCartUser(){
        Cart existingCart = cartRepository.findCartByEmail(authUtils.loggedInEmail());
        if(existingCart != null){
            return existingCart;
        }
        Cart cart = new Cart();
        cart.setCartUser(authUtils.loggedInUser());
        cart.setTotalPrice(0.00);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> findAllCarts(){
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty()){
            throw new APIException("No cart exists.");
        }
        List<CartDTO> cartDTOs = new ArrayList<>();
        carts.forEach(cart -> {
            CartDTO cartDTO = cartMapper.toCartDTO(cart);
            cartDTOs.add(cartDTO);
        });
        return cartDTOs;
    }

    @Override
    public CartDTO getCartOfTheSpecificUserEmail(){
        Cart cart = cartRepository.findCartByUseremail(authUtils.loggedInEmail())
            .orElseThrow(() -> new ResourcesNotFoundException("Cart", "user email", authUtils.loggedInEmail()));

        return cartMapper.toCartDTO(cart);
    }

//    @Override
//    public ApiResponse<?> getCartByEmailAndCartId(){
//        String email = authUtils.loggedInEmail();
//
//        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);
//
//        if(cart == null){
//            throw new ResourcesNotFoundException("Cart", "id", cartId);
//        }
//        return ApiResponse.builder()
//            .status(HttpStatus.OK.toString())
//            .message("Found cart with email id " + emailId + " and id " + cartId)
//            .response(cartMapper.toCartDTO(cart))
//            .build();
//    }


    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        String email = authUtils.loggedInEmail();

        Cart cart = cartRepository.findCartByEmail(email);
        if(cart == null){
            throw new ResourcesNotFoundException("Cart", "user's email", email);
        }

        Long cartId = cart.getCartId();

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourcesNotFoundException("Product", "id", productId));

        if(product.getQuantity() == 0){
            throw new APIException(product.getProductName() + " is not available.");
        }

        if(quantity > product.getQuantity()){
            throw new APIException("Please make order of the " + product.getProductName() + " less than or equal to " + product.getQuantity() + ".");
        }

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
        if(cartItem == null){
            throw new ResourcesNotFoundException("Item", "name", product.getProductName());
        }

        int newQuantity = cartItem.getQuantity() + quantity;
        if(newQuantity < 0){
            throw new APIException("The resulting quantity can not be negative.");
        }

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));

        if(updatedCartItem.getQuantity() == 0){
            cart.getCartItems().remove(updatedCartItem);
            cartItemRepository.deleteById(updatedCartItem.getCartItemId());
        }

        cartRepository.save(cart);

        return cartMapper.toCartDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO deleteItemInCart(Long cartId, Long productId){
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourcesNotFoundException("Cart", "id", cartId));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourcesNotFoundException("Product", "id", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new ResourcesNotFoundException("Item", "name", product.getProductName() + " in your cart.");
        } else {
            cart.getCartItems().remove(cartItem);
            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
            cartItemRepository.deleteById(cartItem.getCartItemId());
            cartRepository.save(cart);
        }

        return cartMapper.toCartDTO(cart);
    }

    @Override
    public CartItemDTO updateItemInCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourcesNotFoundException("Cart", "id", cartId));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourcesNotFoundException("Product", "id", productId));

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

        if(cartItem == null){
            throw new ResourcesNotFoundException("Item", "name", product.getProductName() + "in your cart.");
        }

        if(cartItem.getQuantity() > product.getQuantity()){
            throw new APIException("Please make order of the " + product.getProductName() + " less than or equal to " + product.getQuantity() + ".");
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem.setProductPrice(product.getSpecialPrice());
        cartItem.setDiscount(product.getDiscount());

        CartItem updatedCartItem = cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + (updatedCartItem.getProductPrice() * cartItem.getQuantity()));
        cartRepository.save(cart);

        return cartItemMapper.toCartItemDTO(updatedCartItem);
    }

}
