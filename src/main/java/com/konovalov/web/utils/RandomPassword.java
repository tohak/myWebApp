package com.konovalov.web.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomPassword {
    private static final int LENGTH = 8;
    private static final boolean USE_LETTERS = true;
    private static final boolean USE_NUMBERS = true;

    public String whenGeneratingRandomString() {
        return RandomStringUtils.random(LENGTH, USE_LETTERS, USE_NUMBERS);
    }
}
