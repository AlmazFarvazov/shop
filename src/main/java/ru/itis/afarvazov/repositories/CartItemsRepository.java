package ru.itis.afarvazov.repositories;

import ru.itis.afarvazov.models.CartItem;

import java.util.List;

public interface CartItemsRepository extends CrudRepository<CartItem, Long>{

    List<CartItem> findByCartId(Long cartId);

}
