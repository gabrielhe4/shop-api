package io.github.gabrielhe4.shop_api.service.impl;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import io.github.gabrielhe4.shop_api.dto.CartDTO;
import io.github.gabrielhe4.shop_api.dto.ProductDTO;
import io.github.gabrielhe4.shop_api.exception.APIException;
import io.github.gabrielhe4.shop_api.exception.ResourceNotFoundException;
import io.github.gabrielhe4.shop_api.mapper.ProductMapper;
import io.github.gabrielhe4.shop_api.model.Cart;
import io.github.gabrielhe4.shop_api.model.CartItem;
import io.github.gabrielhe4.shop_api.model.Product;
import io.github.gabrielhe4.shop_api.repository.CartItemRepository;
import io.github.gabrielhe4.shop_api.repository.CartRepository;
import io.github.gabrielhe4.shop_api.repository.ProductRepository;
import io.github.gabrielhe4.shop_api.service.CartService;
import io.github.gabrielhe4.shop_api.util.AuthUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final AuthUtil authUtil;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();
        
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ResourceNotFoundException("Product", "id", productId)
        );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cart.getId());

        if (cartItem != null)
            throw new APIException(String.format("Product %s already exists!!!", product.getName()));

        if (product.getQuantity() == 0)
            throw new APIException(String.format("Product %s is out of stock!!!", product.getName()));

        if (product.getQuantity() < quantity)
            throw new APIException(String.format("Product %s only has %d available!!!", 
                product.getName(), product.getQuantity()));

        CartItem newCartItem = CartItem.builder()
                                    .cart(cart)
                                    .product(product)
                                    .quantity(quantity)
                                    .discount(product.getDiscount())
                                    .productPrice(product.getSpecialPrice())
                                    .build();
        
        cartItemRepository.save(newCartItem);

        List<CartItem> cartItems = cart.getCartItems();

        Stream<ProductDTO> productsStream = cartItems.stream()
            .map(item -> {
                ProductDTO dto = ProductMapper.INSTANCE.toDTO(item.getProduct());
                dto.setQuantity(item.getQuantity());
                return dto;
            });
        
        return new CartDTO(cart.getId(), 
                cart.getTotalPrice(),
                productsStream.toList()
            );

    }

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.getLoggedInEmail());

        if (userCart == null) 
            return userCart;

        Cart cart = new Cart(authUtil.getLoggedInUser(), 0.00);
        return cartRepository.save(cart);
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();

        if (carts.isEmpty())
            return List.of();

        return carts.stream()
                .map(cart -> new CartDTO(cart.getId(), cart.getTotalPrice(), 
                    cart.getCartItems()
                        .stream()
                        .map(item -> ProductMapper.INSTANCE.toDTO(item.getProduct()))
                        .toList()
                ))
                .toList();
                
    }

    @Override
    public CartDTO getCart() {
        String email = authUtil.getLoggedInEmail();
        Cart cart = cartRepository.findCartByEmail(email);
        Cart newCart = cartRepository.findCartByEmailAndCartId(email, cart.getId());
        
        if (newCart == null)
            throw new ResourceNotFoundException("Cart", "cartId", cart.getId());

        // TODO 
        return new CartDTO(newCart.getId(), cart.getTotalPrice(), 
            cart.getCartItems()
                .stream()
                .map(item -> ProductMapper.INSTANCE.toDTO(item.getProduct()))
                .toList()
        );
    }

    @Override
    public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
        
        String email = authUtil.getLoggedInEmail();
        Cart userCart = cartRepository.findCartByEmail(email);
        Long cartId = userCart.getId();

         Cart cart = cartRepository.findById(cartId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cart", "cartId", cartId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getQuantity() == 0)
            throw new APIException(product.getName() + " is not available");

        if (product.getQuantity() < quantity)
            throw new APIException(String.format("Please, make an order of the %s less than or equal to the quantity %s.", 
                product.getName(), product.getQuantity())
            );

         CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if (cartItem == null)
            throw new APIException(String.format("Product %s not available in the cart!!!", product.getName()));

        int newQuantity = cartItem.getQuantity() + quantity;

        if (newQuantity < 0)
            throw new APIException("The resulting quantity cannot be negative.");

        if (newQuantity == 0) {
            deleteProductFromCart(cartId, productId);
        } else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice() * quantity));
            cartRepository.save(cart);
        }

        CartItem updatedItem = cartItemRepository.save(cartItem);
        if (updatedItem.getQuantity() == 0)
            cartItemRepository.deleteById(updatedItem.getId());

        return new CartDTO(cart.getId(), cart.getTotalPrice(),
                cart.getCartItems().stream()
                        .map(item -> {
                            ProductDTO dto = ProductMapper.INSTANCE.toDTO(item.getProduct());
                            dto.setQuantity(item.getQuantity());
                            return dto;
                        })
                        .toList());
        
    }

    @Override
    public String deleteProductFromCart(Long cartId, Long productId) {
        
        Cart cart = cartRepository.findById(productId).orElseThrow(
            () -> new ResourceNotFoundException("Cart", "cartId", cartId)
        );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);

        if (cartItem == null)
            throw new ResourceNotFoundException("Product", "product", productId);

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

        return String.format("Product %s were removed from the cart", cartItem.getProduct().getName());
    }

    @Override
    public void updateProductInCarts(Long cartId, Long productId) {
        
        Cart cart = cartRepository.findById(cartId).orElseThrow(
            () -> new ResourceNotFoundException("Cart", "cartId", cartId)
        ); 

        Product product = productRepository.findById(productId).orElseThrow(
            () -> new ResourceNotFoundException("Product", "id", productId)
        );

        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId, cartId);


         if (cartItem == null)
            throw new APIException("Product " + product.getName() + " not available in the cart");

        double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());

        cartItem.setProductPrice(product.getSpecialPrice());

        cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));

        cartItem = cartItemRepository.save(cartItem);

    }

}
