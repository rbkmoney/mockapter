package com.rbkmoney.mockapter.model.response.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.woody.api.flow.error.WUnavailableResultException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class UnavailableResultError implements Error {

    private final String reason;

    @JsonCreator
    public UnavailableResultError(@JsonProperty("reason") String reason) {
        this.reason = reason;
    }

    @Override
    public RuntimeException newException() {
        return new WUnavailableResultException(reason);
    }

}
