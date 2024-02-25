package com.seedoilz.cli.utils;

import picocli.CommandLine.Option;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class OptionUtil {
    public static String[] processInteractiveOptions(Class<?> clazz, String[] args) {
        Set<String> argSet = new LinkedHashSet<>(Arrays.asList(args));
        for (Field field : clazz.getDeclaredFields()) {
            Option option = field.getAnnotation(Option.class);
            if (option != null && option.interactive()) {
                if (!argSet.contains(option.names()[0])) {
                    argSet.add(option.names()[0]);
                }
            }
        }
        args = argSet.toArray(new String[0]);
        return args;
    }
}
