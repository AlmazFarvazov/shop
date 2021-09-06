package ru.itis.afarvazov.models;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer extends User {

    private Long id;
    private String email;
    private String login;
    private String hashPassword;

    private Role role;

}
