package org.chervyakovsky.multithreading.util;

public class GeneratorId {

    private static int counter;

    private GeneratorId() {
    }

    public static int generate() {
        return ++counter;
    }
}
