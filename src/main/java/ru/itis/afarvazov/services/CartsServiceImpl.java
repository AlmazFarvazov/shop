package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.repositories.CartsRepository;

import java.util.List;

@Service
public class CartsServiceImpl implements CartsService {

    private final CartsRepository cartsRepository;

    public CartsServiceImpl(CartsRepository repository) {
        this.cartsRepository = repository;
    }

    @Override
    public Cart getCartById(Long id) {
        return cartsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cart not found!"));
    }

    @Override
    public List<Cart> getCartForCustomer(long customerId, boolean active) {

        return cartsRepository.findByOwnerId(customerId, active);
    }

    @Override
    public void createCart(Cart cart) {
        cartsRepository.save(cart);
    }

    @Override
    public void editCart(Cart cart) {
        cartsRepository.update(cart);
    }

    @Override
    public void deleteCart(Cart cart) {
        cartsRepository.delete(cart);
    }


}
