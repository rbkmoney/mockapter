package com.rbkmoney.mockapter.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.response.intent.Intent;
import lombok.ToString;

@ToString
public class IntentResult implements Result {

    @JsonInclude
    private final Intent intent;

    @JsonCreator
    public IntentResult(@JsonInclude Intent intent) {
        this.intent = intent;
    }

    @Override
    public ExitStateModel buildResult(EntryStateModel entryStateModel) {
        return intent.buildResult(entryStateModel);
    }

}
