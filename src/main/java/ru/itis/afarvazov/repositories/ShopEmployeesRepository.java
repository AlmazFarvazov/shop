package ru.itis.afarvazov.repositories;

import ru.itis.afarvazov.models.ShopEmployee;

import java.util.Optional;

public interface ShopEmployeesRepository extends CrudRepository<ShopEmployee, Long>{
    Optional<ShopEmployee> findByEmail(String email);
    Optional<ShopEmployee> findByLogin(String login);
    Optional<ShopEmployee> findByFirstNameAndLastName(String firstName, String lastName);
}
