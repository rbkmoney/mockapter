package com.rbkmoney.mockapter.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.response.error.Error;
import lombok.ToString;

@ToString
public class ErrorResult implements Result {

    @JsonInclude
    private final Error errorDefinition;

    @JsonCreator
    public ErrorResult(@JsonInclude Error errorDefinition) {
        this.errorDefinition = errorDefinition;
    }

    @Override
    public ExitStateModel buildResult(EntryStateModel entryStateModel) {
        throw errorDefinition.newException();
    }
}
