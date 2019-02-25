package com.rbkmoney.mockapter.handler;

import com.rbkmoney.damsel.proxy_provider.*;
import com.rbkmoney.mockapter.converter.entry.CtxToEntryModelConverter;
import com.rbkmoney.mockapter.converter.entry.RecCtxToEntryStateModelConverter;
import com.rbkmoney.mockapter.converter.exit.ExitModelToProxyCallbackResultConverter;
import com.rbkmoney.mockapter.converter.exit.ExitModelToProxyResultConverter;
import com.rbkmoney.mockapter.converter.exit.ExitModelToRecTokenCallbackResultConverter;
import com.rbkmoney.mockapter.converter.exit.ExitModelToRecTokenProxyResultConverter;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.Method;
import com.rbkmoney.mockapter.service.RequestStubService;
import lombok.RequiredArgsConstructor;
import org.apache.thrift.TException;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

@Component
@RequiredArgsConstructor
public class MockapterHandler implements ProviderProxySrv.Iface {

    private final RequestStubService requestStubService;
    private final CtxToEntryModelConverter ctxToEntryModelConverter;
    private final RecCtxToEntryStateModelConverter recCtxToEntryStateModelConverter;
    private final ExitModelToProxyResultConverter exitModelToProxyResultConverter;
    private final ExitModelToRecTokenProxyResultConverter exitModelToRecTokenProxyResultConverter;
    private final ExitModelToProxyCallbackResultConverter exitModelToProxyCallbackResultConverter;
    private final ExitModelToRecTokenCallbackResultConverter exitModelToRecTokenCallbackResultConverter;


    @Override
    public RecurrentTokenProxyResult generateToken(RecurrentTokenContext context) throws TException {
        EntryStateModel entryStateModel = recCtxToEntryStateModelConverter.convert(context);
        entryStateModel.setMethod(Method.generate_token);
        ExitStateModel exitStateModel = requestStubService.processRequest(entryStateModel);
        return exitModelToRecTokenProxyResultConverter.convert(exitStateModel);
    }

    @Override
    public RecurrentTokenCallbackResult handleRecurrentTokenCallback(ByteBuffer callback, RecurrentTokenContext context) throws TException {
        EntryStateModel entryStateModel = recCtxToEntryStateModelConverter.convert(context);
        entryStateModel.setMethod(Method.handle_recurrent_token_callback);
        ExitStateModel exitStateModel = requestStubService.processRequest(entryStateModel);
        return exitModelToRecTokenCallbackResultConverter.convert(exitStateModel);
    }

    @Override
    public PaymentProxyResult processPayment(PaymentContext context) throws TException {
        EntryStateModel entryStateModel = ctxToEntryModelConverter.convert(context);
        entryStateModel.setMethod(Method.process_payment);
        ExitStateModel exitStateModel = requestStubService.processRequest(entryStateModel);
        return exitModelToProxyResultConverter.convert(exitStateModel);
    }

    @Override
    public PaymentCallbackResult handlePaymentCallback(ByteBuffer callback, PaymentContext context) throws TException {
        EntryStateModel entryStateModel = ctxToEntryModelConverter.convert(context);
        entryStateModel.setMethod(Method.handle_payment_callback);
        ExitStateModel exitStateModel = requestStubService.processRequest(entryStateModel);
        return exitModelToProxyCallbackResultConverter.convert(exitStateModel);
    }
}
