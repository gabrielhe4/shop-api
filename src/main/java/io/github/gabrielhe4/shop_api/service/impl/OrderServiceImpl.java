package io.github.gabrielhe4.shop_api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.OrderDTO;
import io.github.gabrielhe4.shop_api.dto.OrderItemDTO;
import io.github.gabrielhe4.shop_api.dto.OrderRequestDTO;
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

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, String paymentMethod, OrderRequestDTO request) {

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

        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.isEmpty())
            throw new APIException("Cart is empty");

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

        cart.getCartItems().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);

            // clear cart
            cartService.deleteProductFromCart(cart.getId(), item.getProduct().getId());
        });

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
            .payment(paymentDTO)
            .orderItems(orderItems)
            .build();
    }

}
