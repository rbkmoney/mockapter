package com.rbkmoney.mockapter.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.mockapter.model.response.delay.Delay;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DelayTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Test
    public void testFixedDelay() throws IOException {
        Delay delay = objectMapper
                .readValue(
                        "{'type': 'fixed', 'value': 500}",
                        Delay.class
                );

        assertEquals(500, delay.nextTimeout());
    }

    @Test
    public void testUniformDelay() throws IOException {
        Delay delay = objectMapper
                .readValue(
                        "{'type': 'uniform', 'lower': 50, 'upper': 75}",
                        Delay.class
                );
        assertTrue(50 <= delay.nextTimeout());
        assertTrue(delay.nextTimeout() <= 75);
    }

    @Test
    public void testLogNormalDelay() throws IOException {
        Delay delay = objectMapper
                .readValue(
                        "{'type': 'lognormal', 'median': 80, 'sigma': 0.1}",
                        Delay.class
                );

            double average = LongStream.rangeClosed(1, 10000)
                    .map(value -> delay.nextTimeout())
                    .average().getAsDouble();
            assertEquals(80, average, 0.8); // 0.8 VASHE POHUI
    }

}
