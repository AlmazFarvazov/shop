package ru.itis.afarvazov.services;

import ru.itis.afarvazov.dto.CartItemDto;
import ru.itis.afarvazov.dto.CartWithItemsDto;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.models.Customer;

import java.util.List;

public interface CartsService {

    List<Cart> getCartForCustomer(long userId, boolean active);
    Cart getCartById(Long id);
    void createCart(Cart cart);
    void editCart(Cart cart);
    void deleteCart(Cart cart);

    void orderCompleted(Customer customer);

    CartWithItemsDto setTotalPrice(CartItemDto cartItemDto, Cart cart);

    CartWithItemsDto getCartForCustomer(Customer customer);
}
