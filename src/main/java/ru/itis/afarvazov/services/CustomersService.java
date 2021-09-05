package ru.itis.afarvazov.services;

import ru.itis.afarvazov.models.Customer;

import java.util.List;

public interface CustomersService {

    Customer getCustomerByLogin(String login);
    Customer getCustomerByEmail(String email);
    void saveCustomer(Customer customer);
    void editCustomer(Customer customer);
    void deleteCustomer(Customer customer);
    List<Customer> getAllCustomers(Customer customer);

}
