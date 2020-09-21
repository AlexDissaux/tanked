package com.andigago.tanked.util;

import java.util.Random;

public class RandomGenerator {

    private static Random random = new Random();

    public static int getBetween(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }
}
