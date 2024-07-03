package org.chervyakovsky.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;
import java.util.TimerTask;

public class BaseTimerTask extends TimerTask {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void run() {
        LOGGER.info("BaseTimerTask started work");
        Base.getInstance().addingRemovingCargoInBase();
        LOGGER.info("BaseTimerTask finished work");
    }

    public void startBaseTimerTask() {
        Timer timer = new Timer(true);
        timer.schedule(this, 200, 7000);
    }
}
