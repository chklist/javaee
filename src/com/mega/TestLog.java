package com.mega;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestLog.class);

    public static void main(String[] args) {
        LOGGER.info("hello");
    }
}
