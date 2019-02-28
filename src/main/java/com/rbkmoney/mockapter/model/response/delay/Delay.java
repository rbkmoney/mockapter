package com.rbkmoney.mockapter.model.response.delay;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = FixedDelay.class, name = "fixed"),
        @JsonSubTypes.Type(value = LogNormalDelay.class, name = "lognormal"),
        @JsonSubTypes.Type(value = UniformDelay.class, name = "uniform")
})
public interface Delay {

    long nextTimeout();

}
