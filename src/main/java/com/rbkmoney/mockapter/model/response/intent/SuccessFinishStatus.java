package com.rbkmoney.mockapter.model.response.intent;

import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.ToString;

@ToString
public class SuccessFinishStatus implements IntentFinishStatus {

    @Override
    public ExitStateModel buildResult(EntryStateModel entryStateModel) {
        return ExitStateModel.builder()
                .entryStateModel(entryStateModel)
                .build();
    }

}
