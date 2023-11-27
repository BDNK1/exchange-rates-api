package com.tranzzo.exchange.util;


import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModelUtil {

    public static <T> T random(Class<T> clazz) {
        return RandomUtil.unitRandom(clazz);
    }

    public static <T> T random(Class<T> clazz, Consumer<T> callback) {
        T random = RandomUtil.unitRandom(clazz);
        callback.accept(random);
        return random;
    }

    public static String randomString() {
        return RandomUtil.unitRandom(String.class);
    }


    public static <T> List<T> generateList(Integer size, Supplier<T> supplier) {
        if (size < 0) {
            throw new IllegalArgumentException("The stream size must be positive");
        }

        return Stream.generate(supplier).limit(size).collect(Collectors.toList());
    }


    public ModelUtil() {
    }

}
