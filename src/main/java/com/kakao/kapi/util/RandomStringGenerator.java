package com.kakao.kapi.util;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomStringGenerator {
    private static final String chars;
    private static final Random random;

    static {
        chars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        random = new Random();
    }

    public static String generator(int length) {
        return IntStream.range(0, length)
                .mapToObj(x -> chars.charAt(random.nextInt(chars.length())))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
