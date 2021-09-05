package ru.itis.afarvazov.services;

import org.springframework.stereotype.Service;
import ru.itis.afarvazov.exceptions.NoSuchUserException;
import ru.itis.afarvazov.models.Customer;
import ru.itis.afarvazov.repositories.CustomersRepository;

import java.util.List;

@Service
public class CustomersServiceImpl implements CustomersService {

    private final CustomersRepository repository;

    public CustomersServiceImpl(CustomersRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer getCustomerByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new NoSuchUserException("User not found!"));
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NoSuchUserException("User not found!"));
    }

    @Override
    public void saveCustomer(Customer customer) {
        repository.save(customer);
    }

    @Override
    public void editCustomer(Customer customer) {
        repository.update(customer);
    }

    @Override
    public void deleteCustomer(Customer customer) {
        repository.delete(customer);
    }

    @Override
    public List<Customer> getAllCustomers(Customer customer) {
        return repository.findAll();
    }
}
