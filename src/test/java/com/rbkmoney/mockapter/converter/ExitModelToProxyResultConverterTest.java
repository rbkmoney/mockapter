package com.rbkmoney.mockapter.converter;

import com.rbkmoney.damsel.base.Timer;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.damsel.proxy_provider.PaymentProxyResult;
import com.rbkmoney.damsel.proxy_provider.SleepIntent;
import com.rbkmoney.mockapter.converter.exit.ExitModelToProxyResultConverter;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.TargetPaymentStatus;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class ExitModelToProxyResultConverterTest {

    private final ExitModelToProxyResultConverter exitModelToProxyResultConverter = new ExitModelToProxyResultConverter();

    @Test
    public void testWhenFailure() {
        Failure failure = new Failure("code");
        PaymentProxyResult paymentProxyResult = exitModelToProxyResultConverter.convert(
                ExitStateModel.builder()
                        .failure(failure)
                        .build()
        );

        assertTrue(paymentProxyResult.getIntent().getFinish().getStatus().isSetFailure());
        assertEquals(failure, paymentProxyResult.getIntent().getFinish().getStatus().getFailure());
    }

    @Test
    public void testWhenSleep() {
        SleepIntent sleepIntent = new SleepIntent(Timer.timeout(5));
        PaymentProxyResult paymentProxyResult = exitModelToProxyResultConverter.convert(
                ExitStateModel.builder()
                        .sleepIntent(sleepIntent)
                        .build()
        );

        assertTrue(paymentProxyResult.getIntent().isSetSleep());
        assertEquals(sleepIntent, paymentProxyResult.getIntent().getSleep());
    }

    @Test
    public void testWhenFinishCapturedStatus() {
        EntryStateModel entryStateModel = EntryStateModel.builder()
                .invoiceId("invoice_id")
                .paymentId("payment_id")
                .trxId("invoice_id.payment_id")
                .targetPaymentStatus(TargetPaymentStatus.CAPTURED)
                .build();

        PaymentProxyResult paymentProxyResult = exitModelToProxyResultConverter.convert(
                ExitStateModel.builder()
                        .entryStateModel(entryStateModel)
                        .build()
        );

        assertTrue(paymentProxyResult.getIntent().getFinish().getStatus().isSetSuccess());
        assertEquals(entryStateModel.getTrxId(), paymentProxyResult.getIntent().getFinish().getStatus().getSuccess().getToken());
    }

    @Test
    public void testWhenFinishProcessedStatus() {
        EntryStateModel entryStateModel = EntryStateModel.builder()
                .invoiceId("invoice_id")
                .paymentId("payment_id")
                .trxId("invoice_id.payment_id")
                .targetPaymentStatus(TargetPaymentStatus.PROCESSED)
                .build();

        PaymentProxyResult paymentProxyResult = exitModelToProxyResultConverter.convert(
                ExitStateModel.builder()
                        .entryStateModel(entryStateModel)
                        .build()
        );

        assertTrue(paymentProxyResult.getIntent().getFinish().getStatus().isSetSuccess());
        assertEquals(entryStateModel.getTrxId(), paymentProxyResult.getIntent().getFinish().getStatus().getSuccess().getToken());
    }

    @Test
    public void testWhenFinishCancelledStatus() {
        EntryStateModel entryStateModel = EntryStateModel.builder()
                .invoiceId("invoice_id")
                .paymentId("payment_id")
                .trxId("invoice_id.payment_id")
                .targetPaymentStatus(TargetPaymentStatus.CANCELLED)
                .build();

        PaymentProxyResult paymentProxyResult = exitModelToProxyResultConverter.convert(
                ExitStateModel.builder()
                        .entryStateModel(entryStateModel)
                        .build()
        );

        assertTrue(paymentProxyResult.getIntent().getFinish().getStatus().isSetSuccess());
        assertNull(paymentProxyResult.getIntent().getFinish().getStatus().getSuccess().getToken());
    }

    @Test
    public void testWhenFinishRefundedStatus() {
        EntryStateModel entryStateModel = EntryStateModel.builder()
                .invoiceId("invoice_id")
                .paymentId("payment_id")
                .trxId("invoice_id.payment_id")
                .targetPaymentStatus(TargetPaymentStatus.REFUNDED)
                .build();

        PaymentProxyResult paymentProxyResult = exitModelToProxyResultConverter.convert(
                ExitStateModel.builder()
                        .entryStateModel(entryStateModel)
                        .build()
        );

        assertTrue(paymentProxyResult.getIntent().getFinish().getStatus().isSetSuccess());
        assertNull(paymentProxyResult.getIntent().getFinish().getStatus().getSuccess().getToken());
    }

}
