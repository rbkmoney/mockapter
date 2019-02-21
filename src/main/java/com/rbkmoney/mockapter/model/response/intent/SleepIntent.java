package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.damsel.base.Timer;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.ToString;

@ToString
public class SleepIntent implements Intent {

    @JsonProperty("timeout")
    private final int timeout;

    @JsonCreator
    public SleepIntent(@JsonProperty("timeout") int timeout) {
        this.timeout = timeout;
    }

    @Override
    public ExitStateModel getResult(EntryStateModel entryStateModel) {
        return ExitStateModel.builder()
                .entryStateModel(entryStateModel)
                .intent(
                        com.rbkmoney.damsel.proxy_provider.Intent.sleep(
                                new com.rbkmoney.damsel.proxy_provider.SleepIntent(
                                        Timer.timeout(timeout)
                                )
                        )
                )
                .build();
    }
}
