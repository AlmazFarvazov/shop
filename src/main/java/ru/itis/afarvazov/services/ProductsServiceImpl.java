package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.models.Product;
import ru.itis.afarvazov.repositories.ProductsRepository;

import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository repository;

    public ProductsServiceImpl(ProductsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found!"));
    }

    @Override
    public List<Product> getProductsByTitle(String title) {
        return repository.findByTitle(title);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Override
    public void addProduct(Product product) {
        repository.save(product);
    }

    @Override
    public void editProduct(Product product) {
        repository.update(product);
    }

    @Override
    public void deleteProduct(Product product) {
        repository.delete(product);
    }
}
