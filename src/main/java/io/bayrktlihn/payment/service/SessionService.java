package io.bayrktlihn.payment.service;

import io.bayrktlihn.payment.model.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {

    private Map<String, Session> sessions = new HashMap<>();


    public Session getSession(String sessionId) {
        if (sessionId == null) {
            return createAndAddSession();
        }

        Session session = sessions.get(sessionId);
        if (session == null) {
            session = createAndAddSession();
        } else if (session.getExpireDate().isBefore(LocalDateTime.now())) {
            sessions.remove(sessionId);
            session = createAndAddSession();
        }
        return session;
    }

    private Session createAndAddSession() {
        Session createdSession = Session.create();
        sessions.put(createdSession.getSessionId(), createdSession);
        return createdSession;
    }

}
