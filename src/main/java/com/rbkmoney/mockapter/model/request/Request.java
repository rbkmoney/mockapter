package com.rbkmoney.mockapter.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.Method;
import com.rbkmoney.mockapter.model.TargetPaymentStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Request {

    private final Method method;

    private final TargetPaymentStatus targetPaymentStatus;

    @JsonCreator
    public Request(
            @JsonProperty(value = "method", required = true) Method method,
            @JsonProperty("target_payment_status") TargetPaymentStatus targetPaymentStatus

    ) {
        this.method = method;
        this.targetPaymentStatus = targetPaymentStatus;
    }

    public boolean isHandle(EntryStateModel entryStateModel) {
        if (method == entryStateModel.getMethod()) {
            if (targetPaymentStatus != null) {
                return targetPaymentStatus == entryStateModel.getTargetPaymentStatus();
            }
            return true;
        }
        return false;
    }

}
