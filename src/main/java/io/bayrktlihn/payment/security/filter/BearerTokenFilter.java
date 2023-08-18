package io.bayrktlihn.payment.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bayrktlihn.payment.dto.RestResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;

@RequiredArgsConstructor
public class BearerTokenFilter extends OncePerRequestFilter {


    private final String base64EncodedJwtSecretKey;
    private final RequestMatcher shouldNotFilterMatcher;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bearerToken = getBearerToken(request);

        if (authentication == null && bearerToken != null) {

            byte[] jwtSecretKey = Base64.getDecoder().decode(base64EncodedJwtSecretKey);
            SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecretKey);

            try {
                Jws<Claims> jws = Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(bearerToken);

                String username = jws.getBody().getSubject();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);


                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } catch (SignatureException e) {
                handleSignatureException(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleSignatureException(HttpServletRequest request, HttpServletResponse response) {

        response.setHeader("Content-Type", "appplication/json;charset=utf-8");
        RestResponseDto failResponse = RestResponseDto.createFailResponse("Invalid token");


        try (PrintWriter writer = response.getWriter()) {
            writer.println(objectMapper.writeValueAsString(failResponse));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return shouldNotFilterMatcher.matches(request);
    }

    private String getBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        boolean isBearerToken = header.startsWith("Bearer ");

        if (isBearerToken) {
            return header.substring("Bearer ".length());
        }
        return null;

    }
}
