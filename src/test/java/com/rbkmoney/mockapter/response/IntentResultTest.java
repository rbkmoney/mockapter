package com.rbkmoney.mockapter.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.damsel.base.Timer;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.damsel.proxy_provider.FinishStatus;
import com.rbkmoney.damsel.proxy_provider.Intent;
import com.rbkmoney.geck.serializer.kit.tbase.TErrorUtil;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.ExitStateModel;
import com.rbkmoney.mockapter.model.response.Result;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class IntentResultTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Test
    public void testFinishSuccessIntent() throws IOException {
        Result result = objectMapper
                .readValue(
                        "{'intent': {'finish':{'success':{}}}}",
                        Result.class
                );
        ExitStateModel exitStateModel = result.getResult(EntryStateModel.builder().build());
        assertNotNull(exitStateModel);
        Intent intent = exitStateModel.getIntent();
        assertNotNull(intent);
        assertTrue(intent.isSetFinish());
        assertTrue(intent.getFinish().getStatus().isSetSuccess());
    }

    @Test
    public void testFinishFailureIntent() throws IOException {
        Result result = objectMapper
                .readValue(
                        "{'intent': {'finish':{'failure':{'reason':'Card unsupported', 'sub':'authorization_failed:payment_tool_rejected:bank_card_rejected:card_unsupported'}}}}",
                        Result.class
                );
        ExitStateModel exitStateModel = result.getResult(EntryStateModel.builder().build());
        assertNotNull(exitStateModel);
        Intent intent = exitStateModel.getIntent();
        assertNotNull(intent);
        assertTrue(intent.isSetFinish());
        FinishStatus finishStatus = intent.getFinish().getStatus();
        assertTrue(finishStatus.isSetFailure());
        Failure failure = finishStatus.getFailure();
        assertEquals("authorization_failed", failure.getCode());
        assertEquals("Card unsupported", failure.getReason());
        assertEquals("authorization_failed:payment_tool_rejected:bank_card_rejected:card_unsupported", TErrorUtil.toStringVal(failure));
    }

    @Test
    public void testSleepIntent() throws IOException {
        Result result = objectMapper
                .readValue(
                        "{'intent': {'sleep':{'timeout': 5000}}}",
                        Result.class
                );
        ExitStateModel exitStateModel = result.getResult(EntryStateModel.builder().build());
        assertNotNull(exitStateModel);
        Intent intent = exitStateModel.getIntent();
        assertNotNull(intent);
        assertTrue(intent.isSetSleep());
        assertTrue(intent.getSleep().isSetTimer());
        Timer timer = intent.getSleep().getTimer();
        assertTrue(timer.isSetTimeout());
        assertEquals(5000, timer.getTimeout());
    }

}
