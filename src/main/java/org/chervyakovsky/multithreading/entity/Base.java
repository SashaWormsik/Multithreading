package org.chervyakovsky.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.util.BaseProperties;
import org.chervyakovsky.multithreading.util.LogStringUtil;
import org.chervyakovsky.multithreading.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Base {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final AtomicBoolean isCreate = new AtomicBoolean(false);
    private static final ReentrantLock lockGetInstance = new ReentrantLock(true);
    private static final ReentrantLock lockGetTerminal = new ReentrantLock(true);
    private static final Condition condition = lockGetTerminal.newCondition();

    private static Base instance;

    private AtomicInteger capacity;
    private AtomicInteger availableCargoInBase;
    private final List<Terminal> terminals;

    public static Base getInstance() {
        if (!isCreate.get()) {
            try {
                lockGetInstance.lock();
                if (instance == null) {
                    instance = new Base();
                    isCreate.set(true);
                }
            } finally {
                lockGetInstance.unlock();
            }
        }
        return instance;
    }

    private Base() {
        BaseProperties properties = BaseProperties.getInstance();
        int countOfTerminals = properties.getNumberOfTerminals();
        this.terminals = new ArrayList<>(countOfTerminals);
        for (int i = 0; i < countOfTerminals; i++) {
            this.terminals.add(new Terminal());
        }
        this.capacity = new AtomicInteger(properties.getBaseCapacity());
        this.availableCargoInBase = new AtomicInteger(properties.getAvailableCargoInBase());
    }

    public AtomicInteger getCapacity() {
        return capacity;
    }

    public AtomicInteger getAvailableCargoInBase() {
        return availableCargoInBase;
    }

    public List<Terminal> getTerminals() {
        return terminals;
    }

    public Terminal getFreeTerminal(Truck truck) throws InterruptedException {
        Terminal terminal;
        truck.setTruckState(Truck.TruckState.WAITING);
        try {
            lockGetTerminal.lock();
            while ((terminal = findFreeTerminal()) == null || checkPossibilityLoadingUnloading(truck)) { // FIXME
                LOGGER.info("\tTruck ID=  {} waiting free terminal", truck.getTruckId());
                condition.await();
            }
            terminal.setIsFree(false);// FIXME
            LOGGER.info(LogStringUtil.getStringForLog(truck, terminal, TimeUtil.getTime()) + " RECEIVED TERMINAL");
        } finally {
            lockGetTerminal.unlock();
        }
        return terminal;
    }

    public void releaseTerminal(Terminal terminal) {
        try {
            lockGetTerminal.lock();
            terminal.setIsFree(true);// FIXME
            LOGGER.info("\tTERMINAL ID {} is free", terminal.getId());
        } finally {
            condition.signalAll();
            lockGetTerminal.unlock();
        }
    }

    private boolean checkPossibilityLoadingUnloading(Truck truck) {
        int cargoInTruck = truck.getCargoQuantity();
        int freeCapacityBase = this.capacity.get() - this.availableCargoInBase.get();
        return cargoInTruck > freeCapacityBase;
    }

    private Terminal findFreeTerminal() {
        Terminal terminal;
        terminal = terminals.stream().
                filter(Terminal::getIsFree).
                findFirst().
                orElse(null);
        return terminal;
    }
}
