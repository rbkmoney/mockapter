package com.rbkmoney.mockapter.converter.exit;

import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.damsel.proxy_provider.*;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.java.damsel.utils.creators.DomainPackageCreators.createTransactionInfo;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.*;


@Component
@RequiredArgsConstructor
public class ExitModelToRecTokenProxyResultConverter implements Converter<ExitStateModel, RecurrentTokenProxyResult> {

    @Override
    public RecurrentTokenProxyResult convert(ExitStateModel exitStateModel) {
        EntryStateModel entryStateModel = exitStateModel.getEntryStateModel();
        if (exitStateModel.hasFailure()) {
            return createErrorRecurrentTokenProxyResult(exitStateModel.getFailure());
        } else if (exitStateModel.hasSleepIntent()) {
            return createRecurrentProxyResultWithSleepIntent(exitStateModel);
        }
        return createRecurrentTokenProxyResult(
                createRecurrentTokenFinishIntentSuccess(entryStateModel.getRecurrentId()),
                new byte[0],
                createTransactionInfo(
                        entryStateModel.getRecurrentId(),
                        entryStateModel.getParameters()
                )

        );
    }

    private RecurrentTokenProxyResult createErrorRecurrentTokenProxyResult(Failure failure) {
        return createRecurrentTokenProxyResultFailure(failure);
    }

    private RecurrentTokenProxyResult createRecurrentProxyResultWithSleepIntent(ExitStateModel exitStateModel) {
        return createRecurrentTokenProxyResult(RecurrentTokenIntent.sleep(exitStateModel.getSleepIntent()));
    }

}

