package com.exsio.hranon;

import java.security.SecureRandom;

class HrAnonUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    static int randomInt(int bound) {
        return RANDOM.nextInt(bound);
    }
}
