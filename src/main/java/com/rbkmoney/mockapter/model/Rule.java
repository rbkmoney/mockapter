package com.rbkmoney.mockapter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.mockapter.model.request.Request;
import com.rbkmoney.mockapter.model.response.Response;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Rule {

    private final Request request;

    private final Response response;

    private final Double weight;

    @JsonCreator
    public Rule(
            @JsonProperty(value = "request", required = true) Request request,
            @JsonProperty(value = "response", required = true) Response response,
            @JsonProperty("weight") Double weight
    ) {
        this.request = request;
        this.response = response;
        this.weight = weight;
    }

    public boolean hasWeight() {
        return weight != null;
    }
}
