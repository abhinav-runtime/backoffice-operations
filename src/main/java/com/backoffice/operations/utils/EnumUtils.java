package com.backoffice.operations.utils;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnumUtils {
	private static final Logger logger = LoggerFactory.getLogger(EnumUtils.class);
    public static <T extends Enum<T>> boolean isNamePresentInEnum(String name, Class<T> enumClass) {
    	boolean isPresent = Arrays.stream(enumClass.getEnumConstants())
                .anyMatch(enumConstant -> enumConstant.name().equals(name));

        if (!isPresent) {
            logger.error("Name '{}' not present in enum '{}'", name, enumClass.getSimpleName());
        }

        return isPresent;
    }
}