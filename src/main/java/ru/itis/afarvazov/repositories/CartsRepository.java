package ru.itis.afarvazov.repositories;

import ru.itis.afarvazov.models.Cart;

import java.util.List;
import java.util.Optional;

public interface CartsRepository extends CrudRepository<Cart, Long> {

    List<Cart> findByOwnerId(long ownerId, boolean active);

}
