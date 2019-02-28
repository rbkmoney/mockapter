package com.rbkmoney.mockapter.model.response.delay;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

@ToString
@EqualsAndHashCode
public class LogNormalDelay implements Delay {

    private final double median;

    private final double sigma;

    @JsonCreator
    public LogNormalDelay(
            @JsonProperty(value = "median", required = true) double median,
            @JsonProperty(value = "sigma", required = true) double sigma
    ) {
        this.median = median;
        this.sigma = sigma;
    }

    @Override
    public long nextTimeout() {
        return Math.round(Math.exp(ThreadLocalRandom.current().nextGaussian() * sigma) * median);
    }
}
