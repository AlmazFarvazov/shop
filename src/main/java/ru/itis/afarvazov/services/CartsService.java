package ru.itis.afarvazov.services;

import ru.itis.afarvazov.models.Cart;

import java.util.List;

public interface CartsService {

    List<Cart> getCartForCustomer(long userId, boolean active);
    Cart getCartById(Long id);
    void createCart(Cart cart);
    void editCart(Cart cart);
    void deleteCart(Cart cart);

}
