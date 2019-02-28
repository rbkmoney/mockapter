package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.damsel.base.Timer;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class SleepIntent implements Intent {

    private final int timeout;

    @JsonCreator
    public SleepIntent(@JsonProperty(value = "timeout", required = true) int timeout) {
        this.timeout = timeout;
    }

    @Override
    public ExitStateModel buildResult(EntryStateModel entryStateModel) {
        return ExitStateModel.builder()
                .entryStateModel(entryStateModel)
                .sleepIntent(new com.rbkmoney.damsel.proxy_provider.SleepIntent(Timer.timeout(timeout)))
                .build();
    }
}
