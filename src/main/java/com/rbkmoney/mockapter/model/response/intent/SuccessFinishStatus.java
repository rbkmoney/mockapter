package com.rbkmoney.mockapter.model.response.intent;

import com.rbkmoney.damsel.proxy_provider.FinishIntent;
import com.rbkmoney.damsel.proxy_provider.FinishStatus;
import com.rbkmoney.damsel.proxy_provider.Intent;
import com.rbkmoney.damsel.proxy_provider.Success;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.ToString;

@ToString
public class SuccessFinishStatus implements IntentFinishStatus {

    @Override
    public ExitStateModel getResult(EntryStateModel entryStateModel) {
        return ExitStateModel.builder()
                .entryStateModel(entryStateModel)
                .intent(Intent.finish(new FinishIntent(FinishStatus.success(new Success()))))
                .build();
    }
    
}
