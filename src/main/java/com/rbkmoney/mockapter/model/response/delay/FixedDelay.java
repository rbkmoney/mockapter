package com.rbkmoney.mockapter.model.response.delay;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FixedDelay implements Delay {

    private final long value;

    @JsonCreator
    public FixedDelay(@JsonProperty(value = "value", required = true) long value) {
        this.value = value;
    }

    @Override
    public long nextTimeout() {
        return value;
    }
}
