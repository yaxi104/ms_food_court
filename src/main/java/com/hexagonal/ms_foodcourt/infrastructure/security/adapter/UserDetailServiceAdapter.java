package com.hexagonal.ms_foodcourt.infrastructure.security.adapter;

import com.hexagonal.ms_foodcourt.domain.spi.IUserFeignPort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceAdapter implements UserDetailsService {

    private final IUserFeignPort userFeignPort;

    public UserDetailServiceAdapter(IUserFeignPort userFeignPort) {
        this.userFeignPort = userFeignPort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userFeignPort.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(""));

        return new User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }

}
