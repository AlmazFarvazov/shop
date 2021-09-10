package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.dto.CartItemDto;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.models.CartItem;
import ru.itis.afarvazov.repositories.CartItemsRepository;

import java.util.List;

@Service
public class CartItemsServiceImpl implements CartItemsService {

    private final CartItemsRepository repository;

    private final ProductsService productsService;

    public CartItemsServiceImpl(CartItemsRepository repository, ProductsService productsService) {
        this.repository = repository;
        this.productsService = productsService;
    }

    @Override
    public List<CartItem> getCartItemsForCart(long cartId) {
        return repository.findByCartId(cartId);
    }

    @Override
    public void createCartItem(CartItem cartItem) {
        repository.save(cartItem);
    }

    @Override
    public void editCartItem(CartItem cartItem) {
        repository.update(cartItem);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        repository.delete(cartItem);
    }

    @Override
    public CartItemDto createCartItem(CartItemDto cartItemDto, Cart cart) {
        CartItem cartItem = CartItem.builder()
                .productId(cartItemDto.getProductId())
                .cartId(cart.getId())
                .price(productsService.getProductById(cartItemDto.getProductId()).getPrice() *
                        cartItemDto.getAmount())
                .amount(cartItemDto.getAmount())
                .build();
        repository.save(cartItem);
        return CartItemDto.from(cartItem);
    }

}
