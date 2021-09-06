package ru.itis.afarvazov.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.afarvazov.dto.EmailPasswordDto;
import ru.itis.afarvazov.dto.ShopEmployeeSignUpForm;
import ru.itis.afarvazov.exceptions.InvalidEmailOrPasswordException;
import ru.itis.afarvazov.exceptions.NoSuchUserException;
import ru.itis.afarvazov.exceptions.UserAlreadyExistException;
import ru.itis.afarvazov.models.Role;
import ru.itis.afarvazov.models.ShopEmployee;
import ru.itis.afarvazov.repositories.ShopEmployeesRepository;
import ru.itis.afarvazov.security.jwt.JwtTokenUtil;

import java.util.List;

@Service
public class ShopEmployeesServiceImpl implements ShopEmployeesService {

    private final ShopEmployeesRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    public ShopEmployeesServiceImpl(ShopEmployeesRepository repository, PasswordEncoder passwordEncoder,
                                    JwtTokenUtil jwtTokenUtil) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public ShopEmployee getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchUserException("No such user!"));
    }

    @Override
    public ShopEmployee getByEmail(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new NoSuchUserException("Email not found!"));
    }

    @Override
    public ShopEmployee getByLogin(String login) {
        return repository.findByLogin(login).orElseThrow(() -> new NoSuchUserException("Login not found!"));
    }

    @Override
    public ShopEmployee getByFirstNameAndLastName(String firstName, String lastName) {
        return repository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new NoSuchUserException("User not found!"));
    }

    @Override
    public List<ShopEmployee> getAllEmployees() {
        return repository.findAll();
    }

    @Override
    public void saveEmployee(ShopEmployee shopEmployee) {
        repository.save(shopEmployee);
    }

    @Override
    public void deleteEmployee(ShopEmployee shopEmployee) {
        repository.delete(shopEmployee);
    }

    @Override
    public void editEmployee(ShopEmployee shopEmployee) {
        repository.delete(shopEmployee);
    }

    @Override
    public void signUp(ShopEmployeeSignUpForm signUpForm) {
        if (repository.findByEmail(signUpForm.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("User already exist!");
        }
        ShopEmployee shopEmployee = ShopEmployee.builder()
                .email(signUpForm.getEmail())
                .login(signUpForm.getLogin())
                .firstName(signUpForm.getFirstName())
                .lastName(signUpForm.getLastName())
                .hashPassword(passwordEncoder.encode(signUpForm.getPassword()))
                .role(Role.SELLER)
                .build();
        saveEmployee(shopEmployee);
    }

    @Override
    public String signIn(EmailPasswordDto emailPasswordDto) {
        ShopEmployee shopEmployee = getByEmail(emailPasswordDto.getEmail());
        if (passwordEncoder.matches(emailPasswordDto.getPassword(), shopEmployee.getHashPassword())) {
            return jwtTokenUtil.generateToken(shopEmployee);
        }
        throw new InvalidEmailOrPasswordException("Employee not found!");
    }
}
