package com.rbkmoney.mockapter.model.response.delay;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.ToString;

@ToString
public class FixedDelay implements Delay {

    @JsonProperty("value")
    private final long value;

    @JsonCreator
    public FixedDelay(@JsonProperty("value") long value) {
        this.value = value;
    }

    @Override
    public long nextTimeout() {
        return value;
    }
}
