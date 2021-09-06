package ru.itis.afarvazov.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.afarvazov.models.Customer;
import ru.itis.afarvazov.models.Role;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class CustomersRepositoryImpl implements CustomersRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from customer;";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from customer where id = :id;";

    //language=SQL
    private static final String SQL_SELECT_BY_EMAIL = "select * from customer where email = :email;";

    //language=SQL
    private static final String SQL_SELECT_BY_LOGIN = "select * from customer where login = :login;";

    //language=SQL
    private static final String SQL_INSERT = "insert into customer(email, login, hash_password, role) " +
            "values (:email, :login, :hashPassword, :role) returning id;";

    //language=SQL
    private static final String SQL_DELETE = "delete from customer where id = :id;";

    //language=SQL
    private static final String SQL_UPDATE = "update customer set " +
            "email = :email, login = :login, hash_password = :hashPassword, role = :role;";

    private final RowMapper<Customer> customerRowMapper = (row, i) -> Customer.builder()
            .id(row.getLong("id"))
            .email(row.getString("email"))
            .login(row.getString("login"))
            .hashPassword(row.getString("hash_password"))
            .role(Role.valueOf(row.getString("role")))
            .build();

    public CustomersRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, customerRowMapper);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        Customer customer;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            customer = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, params, customerRowMapper);
        } catch (EmptyResultDataAccessException e) {
            customer = null;
        }
        return Optional.ofNullable(customer);
    }

    @Override
    public void save(Customer entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", entity.getEmail());
        params.put("login", entity.getLogin());
        params.put("hashPassword", entity.getHashPassword());
        params.put("role", entity.getRole().name());
        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, parameterSource, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Customer entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", entity.getEmail());
        params.put("login", entity.getLogin());
        params.put("hashPassword", entity.getHashPassword());
        params.put("role", entity.getRole().name());
        jdbcTemplate.update(SQL_UPDATE, params);
    }

    @Override
    public void delete(Customer entity) {
        Map<String, Object> params = Collections.singletonMap("id", entity.getId());
        jdbcTemplate.update(SQL_DELETE, params);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        Customer customer;
        Map<String, Object> params = Collections.singletonMap("email", email);
        try {
            customer = jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, params, customerRowMapper);
        } catch (EmptyResultDataAccessException e) {
            customer = null;
        }
        return Optional.ofNullable(customer);
    }

    @Override
    public Optional<Customer> findByLogin(String login) {
        Customer customer;
        Map<String, Object> params = Collections.singletonMap("login", login);
        try {
            customer = jdbcTemplate.queryForObject(SQL_SELECT_BY_LOGIN, params, customerRowMapper);
        } catch (EmptyResultDataAccessException e) {
            customer = null;
        }
        return Optional.ofNullable(customer);
    }
}
