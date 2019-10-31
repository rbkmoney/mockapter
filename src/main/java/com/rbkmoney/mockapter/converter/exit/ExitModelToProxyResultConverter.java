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
        if (exitStateModel.hasFailure()) {
            return createProxyResultFailure(exitStateModel.getFailure());
        } else if (exitStateModel.hasSleepIntent()) {
            return createPaymentProxyResultWithSleepIntent(exitStateModel);
        }

        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        TransactionInfo transactionInfo = createTransactionInfo(
                entryStateModel.getTrxId(),
                Collections.emptyMap()
        );
        return ProxyProviderPackageCreators.createPaymentProxyResult(
                buildFinishIntent(entryStateModel),
                new byte[0],
                transactionInfo
        );
    }

    private Intent buildFinishIntent(EntryStateModel entryStateModel) {
        switch (entryStateModel.getTargetPaymentStatus()) {
            case PROCESSED:
            case CAPTURED:
                return createFinishIntentSuccessWithToken(entryStateModel.getInvoiceId() + "." + entryStateModel.getPaymentId());
            default:
                return createFinishIntentSuccess();
        }
    }

    private PaymentProxyResult createPaymentProxyResultWithSleepIntent(ExitStateModel exitStateModel) {
        return createPaymentProxyResult(Intent.sleep(exitStateModel.getSleepIntent()));
    }
}

