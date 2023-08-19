package io.bayrktlihn.payment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Session {
    private String sessionId;
    private LocalDateTime createdDate;
    private LocalDateTime expireDate;


    public static Session create() {
        LocalDateTime now = LocalDateTime.now();

        Session session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        session.setCreatedDate(now);
        session.setExpireDate(now.plusDays(1));
        return session;

    }
}
