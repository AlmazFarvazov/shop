package ru.itis.afarvazov.security.jwt.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    public JwtTokenAuthenticationProvider(@Qualifier("customUserDetailsService")UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtTokenAuthentication tokenAuthentication = (JwtTokenAuthentication) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        tokenAuthentication.setAuthenticated(true);
        tokenAuthentication.setUserDetails(userDetails);
        return tokenAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(JwtTokenAuthentication.class);
    }
}
