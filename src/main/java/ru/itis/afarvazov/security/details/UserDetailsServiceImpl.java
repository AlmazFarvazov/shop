package ru.itis.afarvazov.security.details;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.itis.afarvazov.repositories.CustomersRepository;
import ru.itis.afarvazov.repositories.ShopEmployeesRepository;
import ru.itis.afarvazov.security.jwt.JwtTokenUtil;
import ru.itis.afarvazov.security.jwt.JwtTokenUtilImpl;

@Component("customUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final JwtTokenUtil tokenUtil;

    private final CustomersRepository customersRepository;
    private final ShopEmployeesRepository shopEmployeesRepository;

    public UserDetailsServiceImpl(JwtTokenUtil tokenUtil, CustomersRepository customersRepository,
                                  ShopEmployeesRepository shopEmployeesRepository) {
        this.tokenUtil = tokenUtil;
        this.customersRepository = customersRepository;
        this.shopEmployeesRepository = shopEmployeesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String username = tokenUtil.getUsername(token);
        if (customersRepository.findByEmail(username).isPresent()) {
            return new UserDetailsImpl(customersRepository.findByEmail(username).get());
        } else {
            return new UserDetailsImpl(shopEmployeesRepository.findByEmail(username).get());
        }
    }
}
