package org.chervyakovsky.multithreading.util;

import java.util.Random;

public class RandomValuesUtil {

    private static final int CORRECTION_FACTOR = 1;

    private static RandomValuesUtil instance;
    private Random random;

    private RandomValuesUtil() {
        random = new Random();
    }

    public static RandomValuesUtil getInstance() {
        if (instance == null) {
            instance = new RandomValuesUtil();
        }
        return instance;
    }

    public boolean getRandomBoolean() {
        return random.nextBoolean();
    }

    public int getRandomInt(int minValue, int maxValue) {
        return random.nextInt(maxValue - minValue + CORRECTION_FACTOR) + minValue;
    }
}
