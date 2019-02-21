package com.rbkmoney.mockapter.response;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.mockapter.model.EntryStateModel;
import com.rbkmoney.mockapter.model.response.Result;
import com.rbkmoney.woody.api.flow.error.WErrorDefinition;
import com.rbkmoney.woody.api.flow.error.WUnavailableResultException;
import com.rbkmoney.woody.api.flow.error.WUndefinedResultException;
import org.junit.Test;

import java.io.IOException;

import static com.rbkmoney.woody.api.flow.error.WErrorType.UNAVAILABLE_RESULT;
import static com.rbkmoney.woody.api.flow.error.WErrorType.UNDEFINED_RESULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ErrorResultTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Test(expected = WUnavailableResultException.class)
    public void testUnavailableResultError() throws IOException {
        Result result = objectMapper
                .readValue(
                        "{'error':{ 'unavailable_result': {'reason': 'Deadline reached'}}}",
                        Result.class
                );

        try {
            result.getResult(EntryStateModel.builder().build());
            fail();
        } catch (WUnavailableResultException ex) {
            WErrorDefinition errorDefinition = ex.getErrorDefinition();
            assertEquals("Deadline reached", errorDefinition.getErrorMessage());
            assertEquals(UNAVAILABLE_RESULT, errorDefinition.getErrorType());
            throw ex;
        }
    }

    @Test(expected = WUndefinedResultException.class)
    public void testUndefinedResultError() throws IOException {
        Result result = objectMapper
                .readValue(
                        "{'error': { 'undefined_result': {'reason': 'Undefined result'}}}",
                        Result.class
                );

        try {
            result.getResult(EntryStateModel.builder().build());
            fail();
        } catch (WUndefinedResultException ex) {
            WErrorDefinition errorDefinition = ex.getErrorDefinition();
            assertEquals("Undefined result", errorDefinition.getErrorMessage());
            assertEquals(UNDEFINED_RESULT, errorDefinition.getErrorType());
            throw ex;
        }
    }

    @Test(expected = RuntimeException.class)
    public void testUnexpectedError() throws IOException {
        Result result = objectMapper
                .readValue(
                        "{'error': {'unexpected_error': {}}}",
                        Result.class
                );

        result.getResult(EntryStateModel.builder().build());
        fail();
    }

}
