package ru.itis.afarvazov.services;

import ru.itis.afarvazov.models.Product;

import java.util.List;

public interface ProductsService {

    List<Product> getAllProducts();
    List<Product> getProductsByTitle(String title);
    List<Product> getProductsByCategory(String category);
    Product getProductById(Long id);
    void addProduct(Product product);
    void editProduct(Product product);
    void deleteProduct(Product product);

}
