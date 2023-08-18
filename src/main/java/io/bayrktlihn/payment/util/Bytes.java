package io.bayrktlihn.payment.util;

import java.security.SecureRandom;

public class Bytes {

    private Bytes() throws InstantiationException {
        throw new InstantiationException();
    }

    public static byte[] create(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return bytes;
    }


}
