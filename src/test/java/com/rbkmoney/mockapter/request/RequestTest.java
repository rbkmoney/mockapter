package com.rbkmoney.mockapter.request;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.Method;
import com.rbkmoney.mockapter.model.TargetPaymentStatus;
import com.rbkmoney.mockapter.model.request.Request;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class RequestTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

    @Test
    public void testRequestMatchingWithMethodAndStatus() throws IOException {
        Request request = objectMapper
                .readValue(
                        "{'method': 'process_payment', 'target_payment_status': 'processed'}",
                        Request.class
                );

        assertTrue(
                request.isHandle(
                        EntryStateModel.builder()
                                .method(Method.PROCESS_PAYMENT)
                                .targetPaymentStatus(TargetPaymentStatus.PROCESSED)
                                .build()
                )
        );

        assertFalse(
                request.isHandle(
                        EntryStateModel.builder()
                                .method(Method.PROCESS_PAYMENT)
                                .build()
                )
        );

        assertFalse(
                request.isHandle(
                        EntryStateModel.builder()
                                .method(Method.GENERATE_TOKEN)
                                .targetPaymentStatus(TargetPaymentStatus.PROCESSED)
                                .build()
                )
        );
    }

    @Test
    public void testRequestMatchingWithMethodWithoutStatus() throws IOException {
        Request request = objectMapper
                .readValue(
                        "{'method': 'process_payment'}",
                        Request.class
                );

        assertTrue(
                request.isHandle(
                        EntryStateModel.builder()
                                .method(Method.PROCESS_PAYMENT)
                                .targetPaymentStatus(TargetPaymentStatus.PROCESSED)
                                .build()
                )
        );

        assertTrue(
                request.isHandle(
                        EntryStateModel.builder()
                                .method(Method.PROCESS_PAYMENT)
                                .build()
                )
        );

        assertFalse(
                request.isHandle(
                        EntryStateModel.builder()
                                .method(Method.GENERATE_TOKEN)
                                .targetPaymentStatus(TargetPaymentStatus.PROCESSED)
                                .build()
                )
        );
    }

    @Test(expected = MismatchedInputException.class)
    public void testRequestMatchingWithoutMethodButWithStatus() throws IOException {
        objectMapper.readValue("{'target_payment_status': 'processed'}", Request.class);
    }

//    @Test
//    public void testWeightValue() throws IOException {
//        Request request = objectMapper.readValue("{'method': 'process_payment', 'weight': 1}", Request.class);
//        assertTrue(request.hasWeight());
//        assertEquals(1.0, request.getWeight(), 0.0);
//        request = objectMapper.readValue("{'method': 'process_payment', 'weight': 0.5555}", Request.class);
//        assertTrue(request.hasWeight());
//        assertEquals(0.5555, request.getWeight(), 0.0);
//        request = objectMapper.readValue("{'method': 'process_payment'}", Request.class);
//        assertFalse(request.hasWeight());
//    }

}
