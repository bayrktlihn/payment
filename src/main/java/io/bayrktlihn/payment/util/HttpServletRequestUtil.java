package io.bayrktlihn.payment.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class HttpServletRequestUtil {

    private static final String[] VALID_IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"};

    public static String getBearerToken(HttpServletRequest request) {
        return getTokenFromAuthorization(request, "Bearer ");
    }

    public static String getBasicToken(HttpServletRequest request) {
        return getTokenFromAuthorization(request, "Basic ");
    }

    private static String getTokenFromAuthorization(HttpServletRequest request, String startWiths) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null) {
            return null;
        }

        if (authorization.startsWith(startWiths)) {
            return authorization.substring(startWiths.length());
        }

        return null;
    }


    public static String getClientIpAddress(HttpServletRequest request) {
        for (String header : VALID_IP_HEADER_CANDIDATES) {
            String ipAddress = request.getHeader(header);
            if (ipAddress != null && !ipAddress.isEmpty() && !"unknown".equalsIgnoreCase(ipAddress)) {
                return ipAddress;
            }
        }
        return request.getRemoteAddr();
    }


}
