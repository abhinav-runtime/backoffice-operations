package com.backoffice.operations.utils;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

    public static String generateRandomOtp() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

}
