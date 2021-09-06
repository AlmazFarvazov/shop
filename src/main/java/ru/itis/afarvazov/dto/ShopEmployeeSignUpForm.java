package ru.itis.afarvazov.dto;

import lombok.Data;

@Data
public class ShopEmployeeSignUpForm {

    private String email;
    private String login;
    private String firstName;
    private String lastName;
    private String password;

}
