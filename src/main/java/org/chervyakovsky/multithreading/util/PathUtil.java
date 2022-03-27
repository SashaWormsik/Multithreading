package org.chervyakovsky.multithreading.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chervyakovsky.multithreading.exception.BaseThreadException;

import java.net.URL;

public class PathUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    public static String getPath(String resourceName) throws BaseThreadException {
        final int startPosition = 6;
        ClassLoader loader = PathUtil.class.getClassLoader();
        URL resource = loader.getResource(resourceName);
        if (resource == null) {
            LOGGER.info("Resource " + resourceName + " is not found");
            throw new BaseThreadException("Resource " + resourceName + " is not found");
        }
        return resource.toString().substring(startPosition);
    }
}
