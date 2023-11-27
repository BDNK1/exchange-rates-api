package com.tranzzo.exchange.util;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import io.github.benas.randombeans.randomizers.text.StringRandomizer;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import static java.lang.Math.abs;

public class RandomUtil {

    private static final Random RANDOM = new Random();

    private static final EnhancedRandom UNIT_RANDOM = new EnhancedRandomBuilder()
            .randomize(String.class, StringRandomizer.aNewStringRandomizer(6, 10, 0)) // make string size fixed and big enough to guarantee uniqueness
            .collectionSizeRange(0, 0) // make all nested collections empty by default
            .randomize(Integer.class, integerRandomizer())
            .randomize(Long.class, longRandomizer())// 3) make integer values positive
            .randomize(BigDecimal.class, bigDecimalRandomizer())
            .build();

    public static <T> T unitRandom(Class<T> type, String... excludedFields) {
        return UNIT_RANDOM.nextObject(type, excludedFields);
    }

    @NotNull
    private static Randomizer<Integer> integerRandomizer() {
        return () -> abs(RANDOM.nextInt() == Integer.MIN_VALUE ? 0 : RANDOM.nextInt()); // Math.abs dont work for Integer.MIN_VALUE
    }

    @NotNull
    private static Randomizer<BigDecimal> bigDecimalRandomizer() {
        return () -> BigDecimal.valueOf(abs(RANDOM.nextDouble() == Double.MIN_VALUE ? 0 : RANDOM.nextDouble())).setScale(10, RoundingMode.HALF_UP); // Math.abs dont work for Integer.MIN_VALUE
    }

    @NotNull
    private static Randomizer<Long> longRandomizer() {
        return () -> abs(RANDOM.nextLong() == Long.MIN_VALUE ? 0 : RANDOM.nextLong());
    }

    private RandomUtil() {
    }

}
