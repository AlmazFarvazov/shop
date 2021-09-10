package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.dto.CartItemDto;
import ru.itis.afarvazov.dto.CartWithItemsDto;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.models.Customer;
import ru.itis.afarvazov.repositories.CartsRepository;

import java.util.List;

@Service
public class CartsServiceImpl implements CartsService {

    private final CartsRepository repository;

    private final ProductsService productsService;

    private final CartItemsService cartItemsService;

    public CartsServiceImpl(CartsRepository repository, ProductsService productsService,
                            CartItemsService cartItemsService) {
        this.repository = repository;
        this.productsService = productsService;
        this.cartItemsService = cartItemsService;
    }

    @Override
    public Cart getCartById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cart not found!"));
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

    @Override
    public void orderCompleted(Customer customer) {
        Cart cart = repository.findByOwnerId(customer.getId(), true).get(0);
        cart.setActive(false);
        repository.update(cart);
    }

    @Override
    public CartWithItemsDto setTotalPrice(CartItemDto cartItemDto, Cart cart) {
        cart.setTotalPrice(cartItemDto.getAmount() * productsService.getProductById(cartItemDto.getProductId())
                .getPrice() + cart.getTotalPrice());
        repository.update(cart);
        return CartWithItemsDto.builder()
                .cartItemDtos(CartItemDto.from(cartItemsService.getCartItemsForCart(cart.getId())))
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    @Override
    public CartWithItemsDto getCartForCustomer(Customer customer) {
        List<Cart> carts = repository.findByOwnerId(customer.getId(), true);
        Cart cart;
        if (carts.isEmpty()) {
            cart = Cart.builder()
                    .ownerId(customer.getId())
                    .totalPrice(0.0)
                    .active(true)
                    .build();
            repository.save(cart);
        } else {
            cart = carts.get(0);
        }
        return CartWithItemsDto.builder()
                .totalPrice(cart.getTotalPrice())
                .cartItemDtos(CartItemDto.from(cartItemsService.getCartItemsForCart(cart.getId())))
                .build();
    }

}
