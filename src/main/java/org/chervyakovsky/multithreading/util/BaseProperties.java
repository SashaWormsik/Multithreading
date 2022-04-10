package org.chervyakovsky.multithreading.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.exception.BaseThreadException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseProperties {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String NAME_PROPERTIES_FAIL = "data/base.properties";
    private static final int DEFAULT_NUMBER_OF_TRUCKS = 10;
    private static final int DEFAULT_NUMBER_OF_TERMINALS = 3;
    private static final int DEFAULT_CAPACITY_BASE = 1000;
    private static final int DEFAULT_AVAILABLE_CARGO_IN_BASE = 400;

    private static BaseProperties instance;

    private int numberOfTrucks;
    private int numberOfTerminals;
    private int baseCapacity;
    private int availableCargoInBase;

    private BaseProperties() {
        try {
            getPropertiesFromFile();
            LOGGER.info("Properties read from file " + NAME_PROPERTIES_FAIL);
        } catch (BaseThreadException exception) {
            getDefaultProperties();
            LOGGER.warn("Default properties are accepted", exception);
        }
    }

    public static BaseProperties getInstance() {
        if (instance == null) {
            instance = new BaseProperties();
        }
        return instance;
    }

    public int getNumberOfTrucks() {
        return numberOfTrucks;
    }

    public int getNumberOfTerminals() {
        return numberOfTerminals;
    }

    public int getBaseCapacity() {
        return baseCapacity;
    }

    public int getAvailableCargoInBase() {
        return availableCargoInBase;
    }

    private void getPropertiesFromFile() throws BaseThreadException {
        String filePath = PathUtil.getPath(NAME_PROPERTIES_FAIL);
        try (FileInputStream fileStream = new FileInputStream(filePath)) {
            Properties property = new Properties();
            property.load(fileStream);
            this.numberOfTrucks = Integer.parseInt(property.getProperty("number_of_trucks"));
            this.numberOfTerminals = Integer.parseInt(property.getProperty("number_of_terminals"));
            this.baseCapacity = Integer.parseInt(property.getProperty("base_capacity"));
            this.availableCargoInBase = Integer.parseInt(property.getProperty("available_cargo_in_base"));
        } catch (IOException exception) {
            LOGGER.warn("Properties not read", exception);
            throw new BaseThreadException("Properties not read", exception);
        }
    }

    private void getDefaultProperties() {
        this.availableCargoInBase = DEFAULT_AVAILABLE_CARGO_IN_BASE;
        this.baseCapacity = DEFAULT_CAPACITY_BASE;
        this.numberOfTerminals = DEFAULT_NUMBER_OF_TERMINALS;
        this.numberOfTrucks = DEFAULT_NUMBER_OF_TRUCKS;
    }
}
