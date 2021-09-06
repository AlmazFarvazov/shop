package ru.itis.afarvazov.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.afarvazov.models.Product;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ProductsRepositoryImpl implements ProductsRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from product where id = :id;";

    //language=SQL
    private static final String SQL_SELECT_BY_CATEGORY = "select * from product where category = :category;";

    //language=SQL
    private static final String SQL_SELECT_BY_TITLE = "select * from product where title ilike :title;";

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from product;";

    //language=SQL
    private static final String SQL_INSERT = "insert into product(title, price, category, available)" +
            "values (:title, :price, :category, :available) returning id;";

    //language=SQL
    private static final String SQL_DELETE = "delete from product where id = :id;";

    //language=SQL
    private static final String SQL_UPDATE = "update product set " +
            "title = :title, price = :price, category = :category, available = :available where id = :id;";

    private RowMapper<Product> productRowMapper = (row, i) -> Product.builder()
            .id(row.getLong("id"))
            .title(row.getString("title"))
            .price(row.getDouble("price"))
            .availableQuantity(row.getInt("available"))
            .category(Product.Category.valueOf(row.getString("category")))
            .build();

    public ProductsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Product> findAll()   {
        return jdbcTemplate.query(SQL_SELECT_ALL, productRowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        Product product;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            product = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, params, productRowMapper);
        } catch (EmptyResultDataAccessException e) {
            product = null;
        }
        return Optional.ofNullable(product);
    }

    @Override
    public void save(Product entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", entity.getTitle());
        params.put("price", entity.getPrice());
        params.put("category", entity.getCategory().name());
        params.put("available", entity.getAvailableQuantity());
        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, parameterSource, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Product entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", entity.getTitle());
        params.put("price", entity.getPrice());
        params.put("category", entity.getCategory().name());
        params.put("available", entity.getAvailableQuantity());
        params.put("id", entity.getId());
        jdbcTemplate.update(SQL_UPDATE, params);
    }

    @Override
    public void delete(Product entity) {
        Map<String, Object> params = Collections.singletonMap("id", entity.getId());
        jdbcTemplate.update(SQL_DELETE, params);
    }

    @Override
    public List<Product> findByTitle(String title) {
        title = "%" + title + "%";
        Map<String, Object> params = Collections.singletonMap("title", title);
        return jdbcTemplate.query(SQL_SELECT_BY_TITLE, params, productRowMapper);
    }

    @Override
    public List<Product> findByCategory(String category) {
        Map<String, Object> params = Collections.singletonMap("category", category);
        return jdbcTemplate.query(SQL_SELECT_BY_CATEGORY, params, productRowMapper);
    }
}
