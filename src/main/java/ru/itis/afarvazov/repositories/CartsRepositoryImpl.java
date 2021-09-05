package ru.itis.afarvazov.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.afarvazov.models.Cart;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CartsRepositoryImpl implements CartsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from cart;";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from cart where id = :id;";

    //language=SQL
    private static final String SQL_SELECT_BY_OWNER_ID = "select * from cart where owner_id = :ownerId " +
            "and active = :active;";

    //language=SQL
    private static final String SQL_INSERT = "insert into cart(owner_id, total_price) " +
            "values (:ownerId, :totalPrice) returning id;";

    //language=SQL
    private static final String SQL_DELETE = "delete from cart where id = :id;";

    //language=SQL
    private static final String SQL_UPDATE = "update cart set " +
            "owner_id = :ownerId, total_price = :totalPrice;";

    private final RowMapper<Cart> cartRowMapper = (row, i) -> Cart.builder()
            .id(row.getLong("id"))
            .ownerId(row.getLong("owner_id"))
            .totalPrice(row.getDouble("total_price"))
            .build();

    public CartsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Cart> findByOwnerId(long ownerId, boolean active) {
        Map<String, Object> params = new HashMap<>();
        params.put("ownerId", ownerId);
        params.put("active", active);
        return jdbcTemplate.query(SQL_SELECT_BY_OWNER_ID, params, cartRowMapper);
    }

    @Override
    public List<Cart> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, cartRowMapper);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        Cart cart;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            cart = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, params, cartRowMapper);
        } catch (EmptyResultDataAccessException e) {
            cart = null;
        }
        return Optional.ofNullable(cart);
    }

    @Override
    public void save(Cart entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("owner_id", entity.getOwnerId());
        params.put("total_price", entity.getTotalPrice());
        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, parameterSource, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Cart entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("owner_id", entity.getOwnerId());
        params.put("total_price", entity.getTotalPrice());
        jdbcTemplate.update(SQL_UPDATE, params);
    }

    @Override
    public void delete(Cart entity) {
        Map<String, Object> params = Collections.singletonMap("id", entity.getId());
        jdbcTemplate.update(SQL_DELETE, params);
    }
}
