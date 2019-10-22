package com.rbkmoney.mockapter.converter.exit;

import com.rbkmoney.damsel.domain.TransactionInfo;
import com.rbkmoney.damsel.proxy_provider.Intent;
import com.rbkmoney.damsel.proxy_provider.PaymentProxyResult;
import com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;

import static com.rbkmoney.java.damsel.utils.creators.DomainPackageCreators.createTransactionInfo;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.*;


@Component
@RequiredArgsConstructor
public class ExitModelToProxyResultConverter implements Converter<ExitStateModel, PaymentProxyResult> {

    @Override
    public PaymentProxyResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        if (exitStateModel.hasFailure()) {
            return createProxyResultFailure(exitStateModel.getFailure());
        } else if (exitStateModel.hasSleepIntent()) {
            return createPaymentProxyResultWithSleepIntent(exitStateModel);
        }

        TransactionInfo transactionInfo = createTransactionInfo(
                entryStateModel.getTrxId(),
                Collections.emptyMap()
        );
        return ProxyProviderPackageCreators.createPaymentProxyResult(
                createFinishIntentSuccessWithToken(entryStateModel.getInvoiceId() + "." + entryStateModel.getPaymentId()),
                new byte[0],
                transactionInfo
        );
    }

    private PaymentProxyResult createPaymentProxyResultWithSleepIntent(ExitStateModel exitStateModel) {
        return createPaymentProxyResult(Intent.sleep(exitStateModel.getSleepIntent()));
    }
}

