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

    public void setIsFree(boolean newValue) {
        this.isFree.set(newValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Terminal terminal = (Terminal) o;
        return this.id == terminal.getId()
                && this.getIsFree() == terminal.getIsFree();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.id) + Boolean.hashCode(this.getIsFree());
    }
}
