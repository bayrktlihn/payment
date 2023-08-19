package io.bayrktlihn.payment.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class HttpServletRequestUtil {

    public static String getBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            return null;
        }

        if (authorization.startsWith("Bearer ")) {
            return authorization.substring("Bearer ".length());
        }

        return null;
    }


}
