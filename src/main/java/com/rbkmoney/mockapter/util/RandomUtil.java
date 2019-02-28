package com.rbkmoney.mockapter.util;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Random;

public class RandomUtil {

    public static <E> E getWeightedRandom(Map<E, Double> weights, Random random) {
        return weights
                .entrySet()
                .stream()
                .map(weight -> new AbstractMap.SimpleEntry<>(weight.getKey(), -Math.log(random.nextDouble()) / weight.getValue()))
                .min(Comparator.comparing(AbstractMap.SimpleEntry::getValue))
                .orElseThrow(IllegalArgumentException::new)
                .getKey();
    }

}
