package ru.itis.afarvazov.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.afarvazov.dto.ProductDto;
import ru.itis.afarvazov.services.ProductsService;

import javax.annotation.security.PermitAll;
import java.util.List;


@RestController
public class ProductsController {

    private final ProductsService productsService;

    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @PermitAll
    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(ProductDto.from(productsService.getAllProducts()));
    }

    @PermitAll
    @GetMapping("/products/category/{category-name}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable("category-name") String name) {
        name = name.toUpperCase();
        return ResponseEntity.ok(ProductDto.from(productsService.getProductsByCategory(name)));
    }

    @PermitAll
    @GetMapping("/products/title/{product-title}")
    public ResponseEntity<List<ProductDto>> getAllProductsByTitle(@PathVariable("product-title") String title) {
        return  ResponseEntity.ok(ProductDto.from(productsService.getProductsByTitle(title)));
    }

}
