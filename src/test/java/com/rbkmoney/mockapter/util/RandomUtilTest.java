package com.rbkmoney.mockapter.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class RandomUtilTest {

    @Test
    public void testWeighedRandom() {
        Map<Integer, Double> weighedMap = new HashMap();
        weighedMap.put(1, 0.70);
        weighedMap.put(2, 0.10);
        weighedMap.put(3, 0.05);
        weighedMap.put(4, 0.15);

        int size = 10000;
        Map<Integer, Long> counted = IntStream.rangeClosed(1, size)
                .mapToObj(i -> RandomUtil.getWeightedRandom(weighedMap, new Random()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        counted.entrySet().forEach(
                countedValue -> assertEquals(
                        weighedMap.get(countedValue.getKey()),
                        (double) countedValue.getValue() / size, (double) size * 0.03 / size) //delta 3%
        );
    }

}
