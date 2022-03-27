package org.chervyakovsky.multithreading;

import org.chervyakovsky.multithreading.creator.CreatorRandomTruck;
import org.chervyakovsky.multithreading.entity.Truck;
import org.chervyakovsky.multithreading.util.TimeUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadMain {
    public static void main(String[] args) {
       CreatorRandomTruck creatorRandomTruck = CreatorRandomTruck.getInstance();
        List<Truck> list = creatorRandomTruck.createListTrucks();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (Truck truck: list) {
            executorService.execute(truck);
        }
        executorService.shutdown();

    }
}
