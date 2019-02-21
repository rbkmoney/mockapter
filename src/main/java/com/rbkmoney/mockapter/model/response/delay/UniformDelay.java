package com.rbkmoney.mockapter.model.response.delay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

@ToString
public class UniformDelay implements Delay {

    @JsonProperty("lower")
    private final int lower;

    @JsonProperty("upper")
    private final int upper;

    public UniformDelay(@JsonProperty("lower") int lower, @JsonProperty("upper") int upper) {
        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public long nextTimeout() {
        return ThreadLocalRandom.current().nextLong(lower, upper + 1);
    }
}
