package org.chervyakovsky.multithreading.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.exception.BaseThreadException;

import java.net.URL;

public class PathUtil {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int START_POSITION = 6;

    public static String getPath(String resourceName) throws BaseThreadException {
        ClassLoader loader = PathUtil.class.getClassLoader();
        URL resource = loader.getResource(resourceName);
        if (resource == null) {
            LOGGER.info("Resource " + resourceName + " is not found");
            throw new BaseThreadException("Resource " + resourceName + " is not found");
        }
        return resource.toString().substring(START_POSITION);
    }
}
