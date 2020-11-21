package com.kakao.kapi.util;

import java.util.Random;

public class RandomNumberGenerator {
    private static final Random random;

    static {
        random = new Random();
    }

    public static int generateInt(int bound) {
        return random.nextInt(bound);
    }
}
