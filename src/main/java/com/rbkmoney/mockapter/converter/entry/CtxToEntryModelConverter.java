package com.rbkmoney.mockapter.converter.entry;

import com.rbkmoney.damsel.proxy_provider.Invoice;
import com.rbkmoney.damsel.proxy_provider.InvoicePayment;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.geck.common.util.TBaseUtil;
import com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.TargetPaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CtxToEntryModelConverter implements Converter<PaymentContext, EntryStateModel> {

    @Override
    public EntryStateModel convert(PaymentContext context) {
        Invoice invoice = context.getPaymentInfo().getInvoice();
        InvoicePayment payment = context.getPaymentInfo().getPayment();
        String invoiceWithPayment = ProxyProviderPackageCreators.createInvoiceWithPayment(context.getPaymentInfo());

        EntryStateModel entryStateModel = EntryStateModel.builder()
                .targetPaymentStatus(
                        TBaseUtil.unionFieldToEnum(
                                context.getSession().getTarget(),
                                TargetPaymentStatus.class
                        )
                )
                .parameters(context.getOptions())
                .invoiceId(invoice.getId())
                .paymentId(payment.getId())
                .trxId(invoiceWithPayment)
                .build();

        return entryStateModel;
    }

}

