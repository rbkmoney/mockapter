package com.rbkmoney.mockapter.model.response.error;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UnavailableResultError.class, name = "unavailable_result"),
        @JsonSubTypes.Type(value = UndefinedResultError.class, name = "undefined_result"),
        @JsonSubTypes.Type(value = UnexpectedError.class, name = "unexpected_error")
})
public interface Error {

    RuntimeException newException();

}
