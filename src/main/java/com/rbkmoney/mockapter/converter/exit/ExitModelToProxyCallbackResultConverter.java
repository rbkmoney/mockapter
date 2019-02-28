package com.rbkmoney.mockapter.converter.exit;

import com.rbkmoney.damsel.proxy_provider.Intent;
import com.rbkmoney.damsel.proxy_provider.PaymentCallbackProxyResult;
import com.rbkmoney.damsel.proxy_provider.PaymentCallbackResult;
import com.rbkmoney.java.damsel.constant.PaymentState;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.java.damsel.utils.creators.DomainPackageCreators.createTransactionInfo;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.*;

@Component
@RequiredArgsConstructor
public class ExitModelToProxyCallbackResultConverter implements Converter<ExitStateModel, PaymentCallbackResult> {
    @Override
    public PaymentCallbackResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();

        if (exitStateModel.hasFailure()) {
            return createCallbackResultFailure(exitStateModel.getFailure());
        } else if (exitStateModel.hasSleepIntent()) {
            return createCallbackResult(
                    PaymentState.SLEEP.getBytes(),
                    new PaymentCallbackProxyResult()
                            .setIntent(Intent.sleep(exitStateModel.getSleepIntent()))
            );
        }

        return createCallbackResult(
                PaymentState.INIT.getBytes(), createCallbackProxyResult(
                        createFinishIntentSuccess(),
                        new byte[0],
                        createTransactionInfo(entryStateModel.getTrxId(), entryStateModel.getParameters())
                )
        );
    }
}
