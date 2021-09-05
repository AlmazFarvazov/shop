package ru.itis.afarvazov.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.afarvazov.models.CartItem;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CartItemsRepositoryImpl implements CartItemsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from cart_item;";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from cart_item where id = :id;";

    //language=SQL
    private static final String SQL_SELECT_BY_CART_ID = "select * from cart_item where cart_id = :cartId;";

    //language=SQL
    private static final String SQL_INSERT = "insert into cart_item(cart_id, product_id, amount, price) " +
            "values (:cartId, :productId, :amount, :price) returning id;";

    //language=SQL
    private static final String SQL_DELETE = "delete from cart_item where id = :id;";

    //language=SQL
    private static final String SQL_UPDATE = "update cart_item set " +
            "cart_id = :cartId, product_id = :produictId, amount = :amount, price = :price";

    private final RowMapper<CartItem> cartItemRowMapper = (row, i) -> CartItem.builder()
            .id(row.getLong("id"))
            .cartId(row.getLong("cart_id"))
            .productId(row.getLong("product_id"))
            .amount(row.getInt("amount"))
            .price(row.getDouble("price"))
            .build();

    public CartItemsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<CartItem> findByCartId(Long cartId) {
        Map<String, Object> params = Collections.singletonMap("cartId", cartId);
        return jdbcTemplate.query(SQL_SELECT_BY_CART_ID, params, cartItemRowMapper);
    }

    @Override
    public List<CartItem> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, cartItemRowMapper);
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        CartItem cartItem;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            cartItem = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, params, cartItemRowMapper);
        } catch (EmptyResultDataAccessException e) {
            cartItem = null;
        }
        return Optional.ofNullable(cartItem);
    }

    @Override
    public void save(CartItem entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("cartId", entity.getCartId());
        params.put("productId", entity.getProductId());
        params.put("amount", entity.getAmount());
        params.put("price", entity.getPrice());
        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, parameterSource, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(CartItem entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("cartId", entity.getCartId());
        params.put("productId", entity.getProductId());
        params.put("amount", entity.getAmount());
        params.put("price", entity.getPrice());
        jdbcTemplate.update(SQL_UPDATE, params);
    }

    @Override
    public void delete(CartItem entity) {
        Map<String, Object> params = Collections.singletonMap("id", entity.getId());
        jdbcTemplate.update(SQL_DELETE, params);
    }
}
