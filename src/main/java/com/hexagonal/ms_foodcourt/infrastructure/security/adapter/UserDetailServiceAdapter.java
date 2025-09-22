package com.hexagonal.ms_foodcourt.infrastructure.security.adapter;

import com.hexagonal.ms_foodcourt.domain.spi.IRoleFeignPort;
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
    private final IRoleFeignPort roleFeignPort;

    public UserDetailServiceAdapter(IUserFeignPort userFeignPort, IRoleFeignPort roleFeignPort) {
        this.userFeignPort = userFeignPort;
        this.roleFeignPort = roleFeignPort;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userFeignPort.getUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(""));
        var role = roleFeignPort.getRoleById(user.getRoleId()).orElseThrow(() -> new UsernameNotFoundException("Role not found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + role.getName()))
        );
    }

}
