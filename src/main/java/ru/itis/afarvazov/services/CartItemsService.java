package ru.itis.afarvazov.services;

import ru.itis.afarvazov.dto.CartItemDto;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.models.CartItem;

import java.util.List;

public interface CartItemsService {

    List<CartItem> getCartItemsForCart(long cartId);
    void createCartItem(CartItem cartItem);
    void editCartItem(CartItem cartItem);
    void deleteCartItem(CartItem cartItem);

    CartItemDto createCartItem(CartItemDto cartItemDto, Cart cart);
}
