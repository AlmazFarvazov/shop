package ru.itis.afarvazov.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.afarvazov.dto.*;
import ru.itis.afarvazov.models.Cart;
import ru.itis.afarvazov.models.CartItem;
import ru.itis.afarvazov.models.User;
import ru.itis.afarvazov.security.jwt.JwtTokenUtil;
import ru.itis.afarvazov.services.CartItemsService;
import ru.itis.afarvazov.services.CartsService;
import ru.itis.afarvazov.services.CustomersService;
import ru.itis.afarvazov.services.ProductsService;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomersService customersService;

    private final CartsService cartsService;

    private final CartItemsService cartItemsService;

    private final ProductsService productsService;

    private final JwtTokenUtil tokenUtil;

    public CustomerController(CustomersService customersService, CartsService cartsService,
                              CartItemsService cartItemsService, ProductsService productsService, JwtTokenUtil tokenUtil) {
        this.customersService = customersService;
        this.cartsService = cartsService;
        this.cartItemsService = cartItemsService;
        this.productsService = productsService;
        this.tokenUtil = tokenUtil;
    }

    @PermitAll
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody CustomerSignUpForm signUpForm) {
        customersService.signUp(signUpForm);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PermitAll
    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> singIn(@RequestBody EmailPasswordDto emailPasswordDto) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(customersService.signIn(emailPasswordDto));
        return ResponseEntity.ok(tokenDto);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cart")
    public ResponseEntity<CartWithItemsDto> getCart(@RequestHeader("X-TOKEN") String token) {
        User user = customersService.getCustomerByEmail(tokenUtil.getUsername(token));
        List<Cart> carts = cartsService.getCartForCustomer(user.getId(), true);
        Cart cart;
        if (carts.isEmpty()) {
            cart = Cart.builder()
                    .ownerId(user.getId())
                    .totalPrice(0.0)
                    .active(true)
                    .build();
            cartsService.createCart(cart);
        } else {
            cart = carts.get(0);
        }
        CartWithItemsDto cartDto = CartWithItemsDto.builder()
                .totalPrice(cart.getTotalPrice())
                .cartItems(cartItemsService.getCartItemsForCart(cart.getId()))
                .build();
        return ResponseEntity.ok(cartDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/cart/new/item")
    public ResponseEntity<CartWithItemsDto> addCartItem(@RequestHeader("X-TOKEN") String token,
                                            @RequestBody CartItemDto cartItemDto) {
        User user = customersService.getCustomerByEmail(tokenUtil.getUsername(token));
        Cart cart = cartsService.getCartForCustomer(user.getId(), true).get(0);
        CartItem cartItem = CartItem.builder()
                .productId(cartItemDto.getProductId())
                .cartId(cart.getId())
                .price(productsService.getProductById(cartItemDto.getProductId()).getPrice() * cartItemDto.getAmount())
                .amount(cartItemDto.getAmount())
                .build();
        cartItemsService.createCartItem(cartItem);
        cart.setTotalPrice(cartItem.getAmount() *
                productsService.getProductById(cartItem.getProductId()).getPrice() + cart.getTotalPrice());
        cartsService.editCart(cart);
        return ResponseEntity.ok(CartWithItemsDto.builder()
                .cartItems(cartItemsService.getCartItemsForCart(cart.getId()))
                .totalPrice(cart.getTotalPrice())
                .build());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/cart/complete")
    public ResponseEntity<CartWithItemsDto> addCartItem(@RequestHeader("X-TOKEN") String token,
                                                        @RequestBody CartWithItemsDto cartWithItemsDto) {
        User user = customersService.getCustomerByEmail(tokenUtil.getUsername(token));
        Cart cart = cartsService.getCartForCustomer(user.getId(), true).get(0);
        cart.setActive(false);
        cartsService.editCart(cart);
        return ResponseEntity.ok(cartWithItemsDto);
    }

}
