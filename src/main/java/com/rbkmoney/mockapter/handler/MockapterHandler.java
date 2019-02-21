package com.rbkmoney.mockapter.handler;

import com.rbkmoney.damsel.proxy_provider.*;
import org.apache.thrift.TException;

import java.nio.ByteBuffer;

public class MockapterHandler implements ProviderProxySrv.Iface {
    @Override
    public RecurrentTokenProxyResult generateToken(RecurrentTokenContext context) throws TException {
        return null;
    }

    @Override
    public RecurrentTokenCallbackResult handleRecurrentTokenCallback(ByteBuffer callback, RecurrentTokenContext context) throws TException {
        return null;
    }

    @Override
    public PaymentProxyResult processPayment(PaymentContext context) throws TException {
        return null;
    }

    @Override
    public PaymentCallbackResult handlePaymentCallback(ByteBuffer callback, PaymentContext context) throws TException {
        return null;
    }
}
