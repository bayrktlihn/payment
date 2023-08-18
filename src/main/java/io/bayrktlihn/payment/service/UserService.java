package io.bayrktlihn.payment.service;

import io.bayrktlihn.payment.dto.LoginRequestDto;
import io.bayrktlihn.payment.dto.LoginResponseDto;
import io.bayrktlihn.payment.entity.Authority;
import io.bayrktlihn.payment.entity.Role;
import io.bayrktlihn.payment.entity.User;
import io.bayrktlihn.payment.exception.InvalidUsernameOrPasswordException;
import io.bayrktlihn.payment.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${base64-encoded-jwt-secret-key}")
    private String base64EncodedJwtSecretKey;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public LoginResponseDto login(LoginRequestDto loginRequest) {

        String username = loginRequest.getUsername();

        User user = userRepository.findUsersWithAuthoritiesAndRolesByUsername(username).orElseThrow(InvalidUsernameOrPasswordException::new);


        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidUsernameOrPasswordException();
        }


        Instant now = Instant.now();
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        List<String> authorities = user.getAuthorities().stream().map(Authority::getName).toList();


        byte[] jwtSecretKey = Base64.getDecoder().decode(base64EncodedJwtSecretKey);
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecretKey);


        String token = Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .setSubject(username)
                .claim("roles", roles)
                .claim("authorities", authorities)
                .compact();


        return LoginResponseDto.builder()
                .token(token)
                .build();
    }


}
