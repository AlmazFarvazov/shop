package ru.itis.afarvazov.repositories;


import ru.itis.afarvazov.models.Product;

import java.util.List;

public interface ProductsRepository extends CrudRepository<Product, Long> {

    List<Product> findByTitle(String title);
    List<Product> findByCategory(String category);

}
