package io.bayrktlihn.payment.security.service;

import io.bayrktlihn.payment.entity.User;
import io.bayrktlihn.payment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BayrktlihnUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUsersWithAuthoritiesAndRolesByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).toList();
        List<SimpleGrantedAuthority> roles = user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())).toList();

        List<SimpleGrantedAuthority> allAuthorities = new ArrayList<>();
        allAuthorities.addAll(authorities);
        allAuthorities.addAll(roles);

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .authorities(allAuthorities)
                .password(user.getPassword())
                .build();
    }
}
