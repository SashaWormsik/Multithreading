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

    public boolean getIsFree() {
        return isFree.get();
    }

    public void setIsFree(boolean newValue){
       this.isFree.set(newValue);
    }
}
