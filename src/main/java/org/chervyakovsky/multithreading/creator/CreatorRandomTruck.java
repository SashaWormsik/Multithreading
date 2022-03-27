package org.chervyakovsky.multithreading.creator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.entity.Truck;
import org.chervyakovsky.multithreading.util.BaseProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatorRandomTruck {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int MAX_CAPACITY = 15;
    private static final int MIN_CAPACITY = 5;
    public static final int CORRECTION_FACTOR_FOR_CAPACITY = 1;
    public static final double LOADING_PERCENTAGE = 0.3;

    private static CreatorRandomTruck instance;

    private final Random random;
    private final int countOfTruck;

    private CreatorRandomTruck() {
        BaseProperties properties = BaseProperties.getInstance();
        this.random = new Random();
        this.countOfTruck = properties.getNumberOfTrucks();
    }

    public static CreatorRandomTruck getInstance() {
        if (instance == null) {
            instance = new CreatorRandomTruck();
        }
        return instance;
    }

    public List<Truck> createListTrucks() {
        List<Truck> listTrucks = new ArrayList<>(this.countOfTruck);
        for (int i = 0; i < countOfTruck; i++) {
            Truck truck = createTruck();
            listTrucks.add(truck);
        }
        LOGGER.info(countOfTruck + " Trucks are created");
        return listTrucks;
    }

    private Truck createTruck() {
        int capacity = random.nextInt(MAX_CAPACITY - MIN_CAPACITY) + MIN_CAPACITY + CORRECTION_FACTOR_FOR_CAPACITY;
        int cargoQuantity = (int) Math.ceil(capacity * LOADING_PERCENTAGE);
        return new Truck(capacity, cargoQuantity);
    }
}
