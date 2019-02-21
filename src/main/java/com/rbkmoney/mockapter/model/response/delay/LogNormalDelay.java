package com.rbkmoney.mockapter.model.response.delay;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

@ToString
public class LogNormalDelay implements Delay {

    @JsonProperty("median")
    private final double median;

    @JsonProperty("sigma")
    private final double sigma;

    @JsonCreator
    public LogNormalDelay(@JsonProperty("median") double median, @JsonProperty("sigma") double sigma) {
        this.median = median;
        this.sigma = sigma;
    }

    @Override
    public long nextTimeout() {
        return Math.round(Math.exp(ThreadLocalRandom.current().nextGaussian() * sigma) * median);
    }
}
