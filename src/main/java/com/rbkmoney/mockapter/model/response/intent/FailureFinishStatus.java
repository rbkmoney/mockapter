package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.damsel.proxy_provider.FinishIntent;
import com.rbkmoney.damsel.proxy_provider.FinishStatus;
import com.rbkmoney.damsel.proxy_provider.Intent;
import com.rbkmoney.geck.serializer.kit.tbase.TErrorUtil;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.ToString;

@ToString
public class FailureFinishStatus implements IntentFinishStatus {

    @JsonProperty("reason")
    public final String reason;

    @JsonProperty("sub")
    public final String sub;

    @JsonCreator
    public FailureFinishStatus(@JsonProperty("reason") String reason, @JsonProperty("sub") String sub) {
        this.reason = reason;
        this.sub = sub;
    }

    @Override
    public ExitStateModel getResult(EntryStateModel entryStateModel) {
        Failure failure = TErrorUtil.toGeneral(sub);
        failure.setReason(reason);

        return ExitStateModel.builder()
                .entryStateModel(entryStateModel)
                .intent(
                        Intent.finish(
                                new FinishIntent(
                                        FinishStatus.failure(failure)
                                )
                        )
                )
                .build();
    }
}
