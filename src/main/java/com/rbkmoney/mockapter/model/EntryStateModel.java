package com.rbkmoney.mockapter.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Data
@Builder
@ToString
public class EntryStateModel {

    private Method method;
    private TargetPaymentStatus targetPaymentStatus;
    private String invoiceId;
    private String paymentId;
    private String refundId;
    private String recurrentId;
    private String trxId;
    private Map<String, String> parameters;

}
