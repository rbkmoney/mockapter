package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FinishIntent implements Intent {

    private final IntentFinishStatus intentFinishStatus;

    @JsonCreator
    public FinishIntent(@JsonInclude IntentFinishStatus intentFinishStatus) {
        this.intentFinishStatus = intentFinishStatus;
    }

    @Override
    public ExitStateModel buildResult(EntryStateModel entryStateModel) {
        return intentFinishStatus.buildResult(entryStateModel);
    }
}
