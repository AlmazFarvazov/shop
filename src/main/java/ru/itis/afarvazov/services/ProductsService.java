package ru.itis.afarvazov.services;

import ru.itis.afarvazov.models.Product;

import java.util.List;

public interface ProductsService {

    List<Product> getProductsByTitle(String title);
    List<Product> getProductsByCategory(String category);
    void addProduct(Product product);
    void editProduct(Product product);
    void deleteProduct(Product product);

}
