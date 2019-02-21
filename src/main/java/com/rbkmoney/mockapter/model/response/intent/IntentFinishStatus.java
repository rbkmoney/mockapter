package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rbkmoney.mockapter.model.response.Result;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SuccessFinishStatus.class, name = "success"),
        @JsonSubTypes.Type(value = FailureFinishStatus.class, name = "failure")
})
public interface IntentFinishStatus extends Result {
}
