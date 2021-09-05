package ru.itis.afarvazov.repositories;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.itis.afarvazov.models.Role;
import ru.itis.afarvazov.models.ShopEmployee;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class ShopEmployeesRepositoryImpl implements ShopEmployeesRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    //language=SQL
    private static final String SQL_SELECT_ALL = "select * from shop_employee;";

    //language=SQL
    private static final String SQL_SELECT_BY_ID = "select * from shop_employee where id = :id;";

    //language=SQL
    private static final String SQL_SELECT_BY_EMAIL = "select * from shop_employee where email = :email;";

    //language=SQL
    private static final String SQL_SELECT_BY_LOGIN = "select * from shop_employee where login = :login;";

    //language=SQL
    private static final String SQL_SELECT_BY_FIRST_NAME_AND_LAST_NAME = "select * from shop_employee " +
            "where first_name = :firstName and last_name = :last_name;";

    //language=SQL
    private static final String SQL_INSERT = "insert into " +
            "shop_employee(email, login, first_name, last_name, hash_password, role) " +
            "values (:email, :login, :firstName, :lastName, :hashPassword, :role) returning id;";

    //language=SQL
    private static final String SQL_DELETE = "delete from shop_employee where id = :id;";

    //language=SQL
    private static final String SQL_UPDATE = "update shop_employee set " +
            "email = :email, login = :login, first_name = :firstName, last_name = :lastName, " +
            "hash_password = :hashPassword, role = :role;";

    private final RowMapper<ShopEmployee> shopEmployeeRowMapper = (row, i) -> ShopEmployee.builder()
            .id(row.getLong("id"))
            .email(row.getString("email"))
            .login(row.getString("login"))
            .firstName(row.getString("first_name"))
            .lastName(row.getString("last_name"))
            .hashPassword(row.getString("hash_password"))
            .role(Role.valueOf(row.getString("role")))
            .build();

    public ShopEmployeesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<ShopEmployee> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, shopEmployeeRowMapper);
    }

    @Override
    public Optional<ShopEmployee> findById(Long id) {
        ShopEmployee shopEmployee;
        Map<String, Object> params = Collections.singletonMap("id", id);
        try {
            shopEmployee = jdbcTemplate.queryForObject(SQL_SELECT_BY_ID, params, shopEmployeeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            shopEmployee = null;
        }
        return Optional.ofNullable(shopEmployee);
    }

    @Override
    public void save(ShopEmployee entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", entity.getEmail());
        params.put("login", entity.getLogin());
        params.put("firstName", entity.getFirstName());
        params.put("lastName", entity.getLastName());
        params.put("hashPassword", entity.getHashPassword());
        params.put("role", entity.getRole().name());
        SqlParameterSource parameterSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(SQL_INSERT, parameterSource, keyHolder);
        entity.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(ShopEmployee entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", entity.getEmail());
        params.put("login", entity.getLogin());
        params.put("firstName", entity.getFirstName());
        params.put("lastName", entity.getLastName());
        params.put("hashPassword", entity.getHashPassword());
        params.put("role", entity.getRole().name());
        jdbcTemplate.update(SQL_UPDATE, params);
    }

    @Override
    public void delete(ShopEmployee entity) {
        Map<String, Object> params = Collections.singletonMap("id", entity.getId());
        jdbcTemplate.update(SQL_DELETE, params);
    }

    @Override
    public Optional<ShopEmployee> findByEmail(String email) {
        ShopEmployee shopEmployee;
        Map<String, Object> params = Collections.singletonMap("email", email);
        try {
            shopEmployee = jdbcTemplate.queryForObject(SQL_SELECT_BY_EMAIL, params, shopEmployeeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            shopEmployee = null;
        }
        return Optional.ofNullable(shopEmployee);
    }

    @Override
    public Optional<ShopEmployee> findByLogin(String login) {
        ShopEmployee shopEmployee;
        Map<String, Object> params = Collections.singletonMap("login", login);
        try {
            shopEmployee = jdbcTemplate.queryForObject(SQL_SELECT_BY_LOGIN, params, shopEmployeeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            shopEmployee = null;
        }
        return Optional.ofNullable(shopEmployee);
    }

    @Override
    public Optional<ShopEmployee> findByFirstNameAndLastName(String firstName, String lastName) {
        ShopEmployee shopEmployee;
        Map<String, Object> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        try {
            shopEmployee = jdbcTemplate.queryForObject(SQL_SELECT_BY_FIRST_NAME_AND_LAST_NAME, params,
                    shopEmployeeRowMapper);
        } catch (EmptyResultDataAccessException e) {
            shopEmployee = null;
        }
        return Optional.ofNullable(shopEmployee);
    }
}
