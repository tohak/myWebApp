package com.konovalov.myWebApp.service.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;

public class RandomPassword {
        public String whenGeneratingRandomString() {
        int length = 8;
        boolean useLetters = true;
        boolean useNumbers = true;
        return  RandomStringUtils.random(length, useLetters, useNumbers);

    }
}
