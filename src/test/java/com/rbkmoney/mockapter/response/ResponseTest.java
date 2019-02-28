package com.rbkmoney.mockapter.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.response.Response;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Test
    public void testResponseWithResultAndDelay() throws IOException {
        Response response = objectMapper
                .readValue(
                        "{'delay':{'type':'fixed','value':1},'result':{'intent':{'sleep':{'timeout':5000}}}}",
                        Response.class
                );
        assertTrue(response.hasDelay());
        assertEquals(1L, response.getDelay().nextTimeout());
        assertEquals(5000,
                response.getResult()
                        .buildResult(EntryStateModel.builder().build())
                        .getSleepIntent().getTimer().getTimeout()
        );
    }

    @Test
    public void testResponseWithResultWithoutDelay() throws IOException {
        Response response = objectMapper
                .readValue(
                        "{'result':{'intent':{'sleep':{'timeout':5000}}}}",
                        Response.class
                );
        assertFalse(response.hasDelay());
        assertEquals(5000,
                response.getResult()
                        .buildResult(EntryStateModel.builder().build())
                        .getSleepIntent().getTimer().getTimeout()
        );
    }

    @Test(expected = MismatchedInputException.class)
    public void testResponseWithDelayWithoutResult() throws IOException {
        objectMapper
                .readValue(
                        "{'delay':{'type':'fixed','value':1}}",
                        Response.class
                );
    }

}
