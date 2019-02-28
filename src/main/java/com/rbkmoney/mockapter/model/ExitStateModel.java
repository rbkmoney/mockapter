package com.rbkmoney.mockapter.model;

import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.damsel.proxy_provider.SleepIntent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ExitStateModel {

    private EntryStateModel entryStateModel;
    private SleepIntent sleepIntent;
    private Failure failure;

    public boolean hasFailure() {
        return failure != null;
    }

    public boolean hasSleepIntent() {
        return sleepIntent != null;
    }

}
