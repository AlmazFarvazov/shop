package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.models.CartItem;
import ru.itis.afarvazov.repositories.CartItemsRepository;

import java.util.List;

@Service
public class CartItemsServiceImpl implements CartItemsService {

    private final CartItemsRepository repository;

    public CartItemsServiceImpl(CartItemsRepository repository) {
        this.repository = repository;
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
}
