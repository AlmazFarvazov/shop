package ru.itis.afarvazov.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopEmployee {

    private long id;
    private String email;
    private String login;
    private String firstName;
    private String lastName;
    private String hashPassword;

    private Role role;
}
