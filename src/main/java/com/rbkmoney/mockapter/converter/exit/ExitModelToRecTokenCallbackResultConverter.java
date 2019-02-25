package com.rbkmoney.mockapter.converter.exit;

import com.rbkmoney.damsel.proxy_provider.RecurrentTokenCallbackResult;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenIntent;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenProxyResult;
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
public class ExitModelToRecTokenCallbackResultConverter implements Converter<ExitStateModel, RecurrentTokenCallbackResult> {

    @Override
    public RecurrentTokenCallbackResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        if (exitStateModel.hasFailure()) {
            return createRecurrentTokenCallbackResultFailure(exitStateModel.getFailure());
        } else if (exitStateModel.hasSleepIntent()) {
            return createRecurrentTokenCallbackResult(
                    new byte[0],
                    new RecurrentTokenProxyResult(RecurrentTokenIntent.sleep(exitStateModel.getSleepIntent()))
            );
        }

        return createRecurrentTokenCallbackResult(
                PaymentState.INIT.getBytes(), createRecurrentTokenProxyResult(
                        createRecurrentTokenFinishIntentSuccess(entryStateModel.getRecurrentId()),
                        new byte[0],
                        createTransactionInfo(entryStateModel.getRecurrentId(), entryStateModel.getParameters())
                )
        );
    }

}

