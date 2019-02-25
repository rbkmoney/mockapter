package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.geck.serializer.kit.tbase.TErrorUtil;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FailureFinishStatus implements IntentFinishStatus {

    public final String sub;

    public final String reason;

    @JsonCreator
    public FailureFinishStatus(
            @JsonProperty(value = "sub", required = true) String sub,
            @JsonProperty("reason") String reason
    ) {
        this.reason = reason;
        this.sub = sub;
    }

    @Override
    public ExitStateModel buildResult(EntryStateModel entryStateModel) {
        Failure failure = TErrorUtil.toGeneral(sub);
        failure.setReason(reason);

        return ExitStateModel.builder()
                .entryStateModel(entryStateModel)
                .failure(failure)
                .build();
    }
}
