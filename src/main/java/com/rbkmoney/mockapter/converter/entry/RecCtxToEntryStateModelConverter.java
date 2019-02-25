package com.rbkmoney.mockapter.converter.entry;

import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.mockapter.model.EntryStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import static com.rbkmoney.java.damsel.utils.extractors.ProxyProviderPackageExtractors.extractRecurrentId;


@Component
@RequiredArgsConstructor
public class RecCtxToEntryStateModelConverter implements Converter<RecurrentTokenContext, EntryStateModel> {

    @Override
    public EntryStateModel convert(RecurrentTokenContext context) {
        String recurrentId = extractRecurrentId(context);
        return EntryStateModel.builder()
                .recurrentId(recurrentId)
                .parameters(context.getOptions())
                .build();
    }
}

