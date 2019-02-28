package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rbkmoney.mockapter.model.response.Result;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FinishIntent.class, name = "finish"),
        @JsonSubTypes.Type(value = SleepIntent.class, name = "sleep")
})
public interface Intent extends Result {
}
