package org.chervyakovsky.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.util.GeneratorId;
import org.chervyakovsky.multithreading.util.LogStringUtil;
import org.chervyakovsky.multithreading.util.RandomValuesUtil;
import org.chervyakovsky.multithreading.util.TimeUtil;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Truck extends Thread {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final int MAX_LOAD_UNLOAD_SPEED = 1000;
    public static final int MIN_LOAD_UNLOAD_SPEED = 500;

    private final int capacity;

    private long truckId;
    private boolean forLoading;
    private boolean isPerishable;
    private TruckState truckState;
    private int cargoQuantity;


    public Truck(int capacity, int cargoQuantity) {
        this.truckId = GeneratorId.generate();
        this.forLoading = RandomValuesUtil.getInstance().getRandomBoolean();
        this.isPerishable = RandomValuesUtil.getInstance().getRandomBoolean();
        this.truckState = TruckState.CREATED;
        this.capacity = capacity;
        this.cargoQuantity = cargoQuantity;
        if (isPerishable) {
            this.setPriority(Thread.MAX_PRIORITY);
        } else {
            this.setPriority(Thread.MIN_PRIORITY);
        }
    }

    public enum TruckState {
        CREATED, WAITING, PROCESSING, COMPLETED
    }

    public long getTruckId() {
        return truckId;
    }

    public boolean isForLoading() {
        return forLoading;
    }

    public void setForLoading(boolean forLoading) {
        this.forLoading = forLoading;
    }

    public boolean isPerishable() {
        return isPerishable;
    }

    public TruckState getTruckState() {
        return truckState;
    }

    public void setTruckState(TruckState truckState) {
        this.truckState = truckState;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCargoQuantity() {
        return cargoQuantity;
    }

    @Override
    public void run() {
        LOGGER.info(LogStringUtil.getStringForLog(this, null, TimeUtil.getTime()));
        Base base = Base.getInstance();
        QueueOfTrucks queueOfTrucks = QueueOfTrucks.getInstance();
        Terminal terminal;
        try {
            queueOfTrucks.getInLine(this);
            terminal = queueOfTrucks.goToBase(this);
            loadUnloadProcess(terminal);
            base.releaseTerminal(terminal);
        } catch (InterruptedException exception) {
            LOGGER.warn("Truck ID {} is interrupted", this.truckId, exception);
        }
    }

    public void loadUnloadProcess(Terminal terminal) throws InterruptedException {
        this.truckState = TruckState.PROCESSING;
        Base base = Base.getInstance();
        if (this.forLoading) {
            LOGGER.info(LogStringUtil.getStringForLog(this, terminal, TimeUtil.getTime()));
            load(base);
        } else {
            LOGGER.info(LogStringUtil.getStringForLog(this, terminal, TimeUtil.getTime()));
            unload(base);
        }
        this.truckState = TruckState.COMPLETED;
        LOGGER.info(LogStringUtil.getStringForLog(this, terminal, TimeUtil.getTime()) + " COMPLETED and release TERMINAL");
    }

    private void load(Base base) throws InterruptedException {
        int quantityPerUpload = this.capacity - this.cargoQuantity;
        int speedLoad = RandomValuesUtil.getInstance().getRandomInt(MIN_LOAD_UNLOAD_SPEED, MAX_LOAD_UNLOAD_SPEED);
        TimeUnit.MILLISECONDS.sleep(speedLoad * quantityPerUpload);
        base.getAvailableCargoInBase().getAndAdd(-quantityPerUpload);
        this.cargoQuantity = this.capacity;
    }

    private void unload(Base base) throws InterruptedException {
        int speedLoad = RandomValuesUtil.getInstance().getRandomInt(MIN_LOAD_UNLOAD_SPEED, MAX_LOAD_UNLOAD_SPEED);
        TimeUnit.MILLISECONDS.sleep(speedLoad * this.cargoQuantity);
        base.getAvailableCargoInBase().getAndAdd(this.cargoQuantity);
        this.cargoQuantity -= this.cargoQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Truck truck = (Truck) o;
        return capacity == truck.capacity &&
                truckId == truck.truckId &&
                forLoading == truck.forLoading &&
                isPerishable == truck.isPerishable &&
                cargoQuantity == truck.cargoQuantity &&
                truckState.equals(truck.truckState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(capacity, truckId, forLoading, isPerishable, truckState, cargoQuantity);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Truck{").
                append("truckId = ").append(this.truckId).
                append(", forLoading = ").append(this.forLoading).
                append(", isPerishable = ").append(this.isPerishable).
                append(", truckState = ").append(this.truckState).
                append(", capacity = ").append(this.capacity).
                append(", cargoQuantity = ").append(this.cargoQuantity).
                append("}");
        return stringBuilder.toString();
    }
}
