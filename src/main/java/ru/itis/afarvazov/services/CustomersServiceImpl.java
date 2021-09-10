package ru.itis.afarvazov.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.afarvazov.dto.CustomerSignUpForm;
import ru.itis.afarvazov.dto.EmailPasswordDto;
import ru.itis.afarvazov.exceptions.InvalidEmailOrPasswordException;
import ru.itis.afarvazov.exceptions.NoSuchUserException;
import ru.itis.afarvazov.exceptions.UserAlreadyExistException;
import ru.itis.afarvazov.models.Customer;
import ru.itis.afarvazov.models.Role;
import ru.itis.afarvazov.repositories.CustomersRepository;
import ru.itis.afarvazov.security.jwt.JwtTokenUtilImpl;

import java.util.List;

@Service
public class CustomersServiceImpl implements CustomersService {

    private final CustomersRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtilImpl jwtTokenUtil;

    public CustomersServiceImpl(CustomersRepository repository, PasswordEncoder passwordEncoder, JwtTokenUtilImpl jwtTokenUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Customer getCustomerByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new NoSuchUserException("Login not found!"));
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NoSuchUserException("Email not found!"));
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

    @Override
    public void signUp(CustomerSignUpForm signUpForm) {
        if (repository.findByEmail(signUpForm.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already signed up!");
        }
        Customer customer = Customer.builder()
                .email(signUpForm.getEmail())
                .login(signUpForm.getLogin())
                .hashPassword(passwordEncoder.encode(signUpForm.getPassword()))
                .role(Role.CUSTOMER)
                .build();
        saveCustomer(customer);
    }

    @Override
    public String signIn(EmailPasswordDto emailPasswordDto) {
        Customer customer = getCustomerByEmail(emailPasswordDto.getEmail());
        if (passwordEncoder.matches(emailPasswordDto.getPassword(), customer.getHashPassword())) {
            return jwtTokenUtil.generateToken(customer);
        }
        throw new InvalidEmailOrPasswordException("Customer not found!");
    }

}
