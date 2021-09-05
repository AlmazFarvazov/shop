package ru.itis.afarvazov.repositories;

import ru.itis.afarvazov.models.Customer;

import java.util.Optional;

public interface CustomersRepository extends CrudRepository<Customer, Long>{

    Optional<Customer> findByEmail(String email);
    Optional<Customer> findByLogin(String login);

}
