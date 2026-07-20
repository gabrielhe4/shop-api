package io.github.gabrielhe4.shop_api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.OrderDTO;
import io.github.gabrielhe4.shop_api.dto.OrderItemDTO;
import io.github.gabrielhe4.shop_api.dto.OrderRequest;
import io.github.gabrielhe4.shop_api.dto.PaymentDTO;
import io.github.gabrielhe4.shop_api.exception.APIException;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.OrderItemMapper;
import io.github.gabrielhe4.shop_api.mapper.PaymentMapper;
import io.github.gabrielhe4.shop_api.mapper.ProductMapper;
import io.github.gabrielhe4.shop_api.model.Address;
import io.github.gabrielhe4.shop_api.model.Cart;
import io.github.gabrielhe4.shop_api.model.CartItem;
import io.github.gabrielhe4.shop_api.model.Order;
import io.github.gabrielhe4.shop_api.model.OrderItem;
import io.github.gabrielhe4.shop_api.model.Payment;
import io.github.gabrielhe4.shop_api.model.Product;
import io.github.gabrielhe4.shop_api.repository.AddressRepository;
import io.github.gabrielhe4.shop_api.repository.CartRepository;
import io.github.gabrielhe4.shop_api.repository.OrderItemRepository;
import io.github.gabrielhe4.shop_api.repository.OrderRepository;
import io.github.gabrielhe4.shop_api.repository.PaymentRepository;
import io.github.gabrielhe4.shop_api.repository.ProductRepository;
import io.github.gabrielhe4.shop_api.service.CartService;
import io.github.gabrielhe4.shop_api.service.OrderService;
import io.github.gabrielhe4.shop_api.util.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final AuthUtil authUtil;

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, String paymentMethod, OrderRequest request) {

        log.info("Place order for user: {}", authUtil.getLoggedInUser());

        Cart cart = cartRepository.findCartByEmail(email);

        if (cart == null)
            throw new ResourceNotFoundException("Cart", "email", email);

        Address address = addressRepository.findById(request.addressId())
            .orElseThrow(
                () -> new ResourceNotFoundException("Address", "addressId", request.addressId()));

        Order order = Order.builder()
                        .email(email)
                        .orderDate(LocalDate.now())
                        .totalAmount(cart.getTotalPrice())
                        .orderStatus("ACCEPTED")
                        .address(address)
                        .build();

        Payment payment = Payment.builder()
                            .id(null)
                            .order(order)
                            .paymentMethod(paymentMethod)
                            .pgPaymentId(request.pgPaymentId())
                            .pgStatus(request.pgStatus())
                            .pgResponseMessage(request.pgResponseMessage())
                            .pgName(request.pgName())
                            .build();

        payment = paymentRepository.save(payment);
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);
        log.info("Payment method added successfully.");

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty())
            throw new APIException("Cart is empty");

        log.info("Total items added in cart: {}", cartItems.size());

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = OrderItem.builder()
                                        .product(cartItem.getProduct())
                                        .order(savedOrder)
                                        .quantity(cartItem.getQuantity())
                                        .discount(cartItem.getDiscount())
                                        .orderedProductPrice(cartItem.getProductPrice())
                                        .build();
            
            orderItems.add(orderItem);
        }

        orderItems = orderItemRepository.saveAll(orderItems);
        savedOrder.setOrderItems(orderItems);
        orderRepository.save(savedOrder);

        cart.getCartItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            // clear cart
            cartService.deleteProductFromCart(item.getProduct().getId());
        });
        log.info("Users cart was cleaned");

        log.info("Order created successfully");

        return buildDTO(savedOrder);
    }


    private OrderDTO buildDTO(Order entity) {
        PaymentDTO paymentDTO = PaymentMapper.INSTANCE.toDTO(entity.getPayment());

        List<OrderItemDTO> orderItems = entity.getOrderItems()
            .stream()
            .map(item -> {
                OrderItemDTO dto = OrderItemMapper.INSTANCE.toDTO(item);
                dto.setProduct(ProductMapper.INSTANCE.toDTO(item.getProduct()));
                return dto;
            })
            .toList();

        return OrderDTO.builder()
            .orderId(entity.getId())
            .addressId(entity.getAddress().getId())
            .email(entity.getEmail())
            .totalAmount(entity.getTotalAmount())
            .orderStatus(entity.getOrderStatus())
            .payment(paymentDTO)
            .orderItems(orderItems)
            .orderDate(entity.getOrderDate())
            .build();
    }

}
