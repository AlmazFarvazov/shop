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
import ru.itis.afarvazov.models.Customer;
import ru.itis.afarvazov.models.User;
import ru.itis.afarvazov.security.jwt.JwtTokenUtil;
import ru.itis.afarvazov.security.jwt.JwtTokenUtilImpl;
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
        Customer user = customersService.getCustomerByEmail(tokenUtil.getUsername(token));
        CartWithItemsDto cartDto = cartsService.getCartForCustomer(user);
        return ResponseEntity.ok(cartDto);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PutMapping("/cart/new/item")
    public ResponseEntity<CartWithItemsDto> addCartItem(@RequestHeader("X-TOKEN") String token,
                                            @RequestBody CartItemDto cartItemDto) {
        Customer user = customersService.getCustomerByEmail(tokenUtil.getUsername(token));
        Cart cart = cartsService.getCartForCustomer(user.getId(), true).get(0);
        cartItemsService.createCartItem(cartItemDto, cart);
        CartWithItemsDto cartWithItemsDto = cartsService.setTotalPrice(cartItemDto, cart);
        return ResponseEntity.ok(cartWithItemsDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/cart/complete")
    public ResponseEntity<CartWithItemsDto> addCartItem(@RequestHeader("X-TOKEN") String token,
                                                        @RequestBody CartWithItemsDto cartWithItemsDto) {
        Customer user = customersService.getCustomerByEmail(tokenUtil.getUsername(token));
        cartsService.orderCompleted(user);
        productsService.orderComplete(cartWithItemsDto);
        return ResponseEntity.ok(cartWithItemsDto);
    }

}
