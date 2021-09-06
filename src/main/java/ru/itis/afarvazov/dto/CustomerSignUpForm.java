package ru.itis.afarvazov.dto;

import lombok.Data;

@Data
public class CustomerSignUpForm {

    private String email;
    private String login;
    private String password;

}
