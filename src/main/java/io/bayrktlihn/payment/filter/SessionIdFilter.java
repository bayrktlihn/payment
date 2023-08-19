package io.bayrktlihn.payment.filter;

import io.bayrktlihn.payment.holder.SessionContextHolder;
import io.bayrktlihn.payment.model.Session;
import io.bayrktlihn.payment.service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SessionIdFilter extends OncePerRequestFilter {

    private final SessionService sessionService;

    public SessionIdFilter(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String sessionId = resolveSessionId(request);

        Session session = sessionService.getSession(sessionId);

        SessionContextHolder.setSession(session);

        response.addHeader("Set-Cookie", "SESSIONID=" + session.getSessionId());

        filterChain.doFilter(request, response);
    }

    private String resolveSessionId(HttpServletRequest request) {
        String sessionId = request.getHeader("SESSION_ID");

        if (sessionId != null && !sessionId.isEmpty()) {
            return sessionId;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SESSIONID")) {
                    return cookie.getValue();
                }
            }
        }

        return request.getParameter("sessionid");
    }
}
