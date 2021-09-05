package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.repositories.CartsRepository;

import java.util.List;

@Service
public class CartsServiceImpl implements CartsService {

    private final CartsRepository repository;

    public CartsServiceImpl(CartsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Cart> getCartForCustomer(long customerId, boolean active) {
        return repository.findByOwnerId(customerId, active);
    }

    @Override
    public void createCart(Cart cart) {
        repository.save(cart);
    }

    @Override
    public void editCart(Cart cart) {
        repository.update(cart);
    }

    @Override
    public void deleteCart(Cart cart) {
        repository.delete(cart);
    }
}
