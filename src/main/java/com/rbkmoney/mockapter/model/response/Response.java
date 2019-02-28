package com.rbkmoney.mockapter.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.mockapter.model.response.delay.Delay;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Response {

    private final Delay delay;

    private final Result result;

    @JsonCreator
    public Response(
            @JsonProperty("delay") Delay delay,
            @JsonProperty(value = "result", required = true) Result result
    ) {
        this.delay = delay;
        this.result = result;
    }

    public boolean hasDelay() {
        return delay != null;
    }
}
