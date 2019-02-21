package com.rbkmoney.mockapter.model.response.intent;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.WRAPPER_OBJECT
)
public interface Intent {
}
