package org.chervyakovsky.multithreading.entity;

import org.chervyakovsky.multithreading.util.GeneratorId;

import java.util.concurrent.atomic.AtomicBoolean;

public class Terminal {

    private int id;
    private final AtomicBoolean isFree;

    public Terminal() {
        this.id = GeneratorId.generate();
        this.isFree = new AtomicBoolean(true);
    }

    public int getId() {
        return id;
    }

    public AtomicBoolean getIsFree() {
        return isFree;
    }

}
