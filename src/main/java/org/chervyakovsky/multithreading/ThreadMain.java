package org.chervyakovsky.multithreading;

import org.chervyakovsky.multithreading.creator.CreatorRandomTruck;
import org.chervyakovsky.multithreading.entity.Base;
import org.chervyakovsky.multithreading.entity.Truck;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadMain {
    public static void main(String[] args) throws InterruptedException {
        CreatorRandomTruck creatorRandomTruck = CreatorRandomTruck.getInstance();
        List<Truck> list = creatorRandomTruck.createListTrucks();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (Truck truck : list) {
            executorService.execute(truck);
            truck.join();
        }
        executorService.shutdown();
        if (executorService.awaitTermination(50, TimeUnit.SECONDS)) {
            System.out.println(Base.getInstance().getAvailableCargoInBase());
        }
    }
}
