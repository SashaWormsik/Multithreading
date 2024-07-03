package org.chervyakovsky.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.util.LogStringUtil;
import org.chervyakovsky.multithreading.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class QueueOfTrucks {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final AtomicBoolean isCreate = new AtomicBoolean(false);
    private static final ReentrantLock lockGetInstance = new ReentrantLock(true);
    private static final ReentrantLock lockGetLine = new ReentrantLock(true);
    private static final Condition condition = lockGetLine.newCondition();

    private static QueueOfTrucks instance;
    private List<Truck> trucks;

    private QueueOfTrucks() {
        trucks = new ArrayList<>();
    }

    public static QueueOfTrucks getInstance() {
        if (!isCreate.get()) {
            try {
                lockGetInstance.lock();
                if (instance == null) {
                    instance = new QueueOfTrucks();
                    isCreate.set(true);
                }
            } finally {
                lockGetInstance.unlock();
            }
        }
        return instance;
    }

    public void getInLine(Truck truck) throws InterruptedException {
        try {
            lockGetLine.lock();
            LOGGER.info(LogStringUtil.getStringForLog(truck, null, TimeUtil.getTime()) + " TOOK A QUEUE");
            this.trucks.add(truck);
        } finally {
            lockGetLine.unlock();
        }
        TimeUnit.MILLISECONDS.sleep(500);
    }

    public Terminal goToBase(Truck truck) throws InterruptedException {
        Base base = Base.getInstance();
        Terminal terminal;
        try {
            lockGetLine.lock();
            while (findTrucksWithPerishableProducts() && !truck.isPerishable()) {
                condition.await();
            }
            terminal = base.getFreeTerminal(truck);
            trucks.remove(truck);
            LOGGER.info(LogStringUtil.getStringForLog(truck, terminal, TimeUtil.getTime()) + " LEFT THE QUEUE");
            condition.signalAll();
        } finally {
            lockGetLine.unlock();
        }
        return terminal;
    }

    private boolean findTrucksWithPerishableProducts() {
        return trucks.stream().anyMatch(Truck::isPerishable);
    }

}
