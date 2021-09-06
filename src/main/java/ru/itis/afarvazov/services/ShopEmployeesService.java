package ru.itis.afarvazov.services;

import ru.itis.afarvazov.dto.EmailPasswordDto;
import ru.itis.afarvazov.dto.ShopEmployeeSignUpForm;
import ru.itis.afarvazov.models.ShopEmployee;

import java.util.List;

public interface ShopEmployeesService {

    ShopEmployee getById(Long id);
    ShopEmployee getByEmail(String email);
    ShopEmployee getByLogin(String login);
    ShopEmployee getByFirstNameAndLastName(String firstName, String lastName);
    List<ShopEmployee> getAllEmployees();
    void saveEmployee(ShopEmployee shopEmployee);
    void deleteEmployee(ShopEmployee shopEmployee);
    void editEmployee(ShopEmployee shopEmployee);
    void signUp(ShopEmployeeSignUpForm signUpForm);
    String signIn(EmailPasswordDto emailPasswordDto);

}
