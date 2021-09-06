package ru.itis.afarvazov.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopEmployee extends User {

    private Long id;
    private String email;
    private String login;
    private String firstName;
    private String lastName;
    private String hashPassword;

    private Role role;
}
