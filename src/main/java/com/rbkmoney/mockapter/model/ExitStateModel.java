package com.rbkmoney.mockapter.model;

import com.rbkmoney.damsel.proxy_provider.Intent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ExitStateModel {

    private EntryStateModel entryStateModel;
    private Intent intent;

}
