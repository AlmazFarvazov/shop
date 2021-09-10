package ru.itis.afarvazov.security.jwt;
import ru.itis.afarvazov.models.User;

public interface JwtTokenUtil {
    String generateToken(User user);
    String getUsername(String token);
}
