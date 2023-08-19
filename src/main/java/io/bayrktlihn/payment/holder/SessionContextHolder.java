package io.bayrktlihn.payment.holder;

import io.bayrktlihn.payment.model.Session;

public class SessionContextHolder {

    private static final ThreadLocal<Session> SESSION = new ThreadLocal<>();


    public static void setSession(Session session) {
        SESSION.set(session);
    }

    public static Session getSession() {
        return SESSION.get();
    }

}
