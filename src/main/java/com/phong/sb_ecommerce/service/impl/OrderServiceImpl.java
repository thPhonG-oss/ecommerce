package com.phong.sb_ecommerce.service.impl;

import com.phong.sb_ecommerce.exception.APIException;
import com.phong.sb_ecommerce.exception.ResourcesNotFoundException;
import com.phong.sb_ecommerce.mapper.OrderMapper;
import com.phong.sb_ecommerce.model.*;
import com.phong.sb_ecommerce.payload.dto.OrderDTO;
import com.phong.sb_ecommerce.repository.*;
import com.phong.sb_ecommerce.service.CartService;
import com.phong.sb_ecommerce.service.OrderService;
import com.phong.sb_ecommerce.utils.AuthUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtils authUtils;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Override
    public OrderDTO placeOrder(Long addressId, String paymentMethod, String pgPaymentId, String pgStatus, String pgName,String pgResponseMessage){

        // getting user cart
        String userEmail = authUtils.loggedInEmail();
        Cart cart = cartRepository.findCartByEmail(userEmail);
        if(cart == null){
            throw new ResourcesNotFoundException("Cart", "user's email", userEmail);
        }
        // create new order with payment info
        Address address = addressRepository.findById(addressId)
            .orElseThrow(() -> new ResourcesNotFoundException("Address", "id", String.valueOf(addressId)));

        Order order = new Order();
        order.setEmail(userEmail);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order Accepted.");
        order.setAddress(address);


        Payment payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setOrder(order);
        payment.setPgPaymentId(pgPaymentId);
        payment.setPgStatus(pgStatus);
        payment.setPgName(pgName);
        payment.setPgResponseMessage(pgResponseMessage);

        Payment savedPayment = paymentRepository.save(payment);

        order.setPayment(savedPayment);
        Order savedOrder = orderRepository.save(order);

        // get items in the cart into order items
        List<CartItem> cartItems = cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("Cart is empty");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem cartItem : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrderedProductPrice(cartItem.getProduct().getPrice());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setProduct(cartItem.getProduct());
            orderItems.add(orderItem);
        }
        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        order.setOrderItems(savedOrderItems);

        // update product stock & clear the cart
        cart.getCartItems().forEach(cartItem -> {
            // reduce stock
           int quantity = cartItem.getQuantity();
           Product product = cartItem.getProduct();
           product.setQuantity(product.getQuantity() - quantity);
           product.getCartItems().remove(cartItem);
           // save product back to database
           Product savedProduct = productRepository.save(product);
           // delete cart item
           cartItemRepository.delete(cartItem);
        });

        cart.setTotalPrice(0.00);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // send back the order summary
        Order updatedOrder = orderRepository.save(savedOrder);

        return orderMapper.toOrderDTO(updatedOrder);
    }
}
