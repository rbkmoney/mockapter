package com.rbkmoney.mockapter.model.response;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = IntentResult.class, name = "intent"),
        @JsonSubTypes.Type(value = ErrorResult.class, name = "error")
})
public interface Result {

    ExitStateModel getResult(EntryStateModel entryStateModel);

}
