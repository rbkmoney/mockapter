package com.rbkmoney.mockapter.model;

import lombok.*;

import java.util.Map;

@Getter
@Setter
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
