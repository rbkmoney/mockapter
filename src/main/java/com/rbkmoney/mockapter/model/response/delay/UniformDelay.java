package com.rbkmoney.mockapter.model.response.delay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

@ToString
@EqualsAndHashCode
public class UniformDelay implements Delay {

    private final int lower;

    private final int upper;

    public UniformDelay(
            @JsonProperty(value = "lower", required = true) int lower,
            @JsonProperty(value = "upper", required = true) int upper
    ) {
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public long nextTimeout() {
        return ThreadLocalRandom.current().nextLong(lower, upper + 1L);
    }
}
