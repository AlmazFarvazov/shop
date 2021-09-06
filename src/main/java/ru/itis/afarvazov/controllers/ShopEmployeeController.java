package ru.itis.afarvazov.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.itis.afarvazov.dto.*;
import ru.itis.afarvazov.models.Product;
import ru.itis.afarvazov.services.ProductsService;
import ru.itis.afarvazov.services.ShopEmployeesService;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/management")
public class ShopEmployeeController {

    private final ShopEmployeesService shopEmployeesService;

    private final ProductsService productsService;

    public ShopEmployeeController(ShopEmployeesService shopEmployeesService, ProductsService productsService) {
        this.shopEmployeesService = shopEmployeesService;
        this.productsService = productsService;
    }

    @PermitAll
    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody ShopEmployeeSignUpForm signUpForm) {
        shopEmployeesService.signUp(signUpForm);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PermitAll
    @PostMapping("/signIn")
    public ResponseEntity<TokenDto> signIn(@RequestBody EmailPasswordDto emailPasswordDto) {
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(shopEmployeesService.signIn(emailPasswordDto));
        return ResponseEntity.ok(tokenDto);
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/add/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody NewProductDto newProductDto) {
        Product product = Product.builder()
                .title(newProductDto.getTitle())
                .category(newProductDto.getCategory())
                .availableQuantity(newProductDto.getAvailableQuantity())
                .price(newProductDto.getPrice())
                .build();
        productsService.addProduct(product);
        return ResponseEntity.ok(ProductDto.from(product));
    }

    @PreAuthorize("hasAuthority('SELLER')")
    @PutMapping("/edit/product")
    public ResponseEntity<ProductDto> editProduct(@RequestBody ProductDto productDto) {
        Product product = Product.builder()
                .id(productDto.getId())
                .price(productDto.getPrice())
                .availableQuantity(productDto.getAvailableQuantity())
                .category(productDto.getCategory())
                .title(productDto.getTitle())
                .build();
        productsService.editProduct(product);
        return ResponseEntity.ok(ProductDto.from(product));
    }

}
