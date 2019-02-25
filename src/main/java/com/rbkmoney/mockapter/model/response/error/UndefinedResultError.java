package com.rbkmoney.mockapter.model.response.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.woody.api.flow.error.WUndefinedResultException;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class UndefinedResultError implements Error {

    @JsonProperty("reason")
    private final String reason;

    @JsonCreator
    public UndefinedResultError(@JsonProperty("reason") String reason) {
        this.reason = reason;
    }

    @Override
    public RuntimeException newException() {
        return new WUndefinedResultException(reason);
    }

}
