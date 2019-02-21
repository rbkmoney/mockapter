package com.rbkmoney.mockapter.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.response.error.Error;
import lombok.ToString;

@ToString
public class ErrorResult implements Result {

    @JsonProperty("error_definition")
    private final Error errorDefinition;

    @JsonCreator
    public ErrorResult(@JsonProperty("error_definition") Error errorDefinition) {
        this.errorDefinition = errorDefinition;
    }

    @Override
    public ExitStateModel getResult(EntryStateModel entryStateModel) {
        throw errorDefinition.newException();
    }
}
