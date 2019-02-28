package com.rbkmoney.mockapter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.rbkmoney.damsel.domain.Failure;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.ProviderProxySrv;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.geck.serializer.kit.mock.MockMode;
import com.rbkmoney.geck.serializer.kit.mock.MockTBaseProcessor;
import com.rbkmoney.geck.serializer.kit.tbase.TBaseHandler;
import com.rbkmoney.geck.serializer.kit.tbase.TErrorUtil;
import com.rbkmoney.mockapter.model.Rule;
import com.rbkmoney.mockapter.service.RequestStubService;
import com.rbkmoney.woody.api.flow.error.WRuntimeException;
import com.rbkmoney.woody.api.flow.error.WUnavailableResultException;
import com.rbkmoney.woody.api.flow.error.WUndefinedResultException;
import com.rbkmoney.woody.thrift.impl.http.THSpawnClientBuilder;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MockapterHandlerTest extends MockapterApplicationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RequestStubService requestStubService;

    @Value("${service}")
    private String serviceName;

    private ProviderProxySrv.Iface client;

    @Before
    public void setupClient() throws URISyntaxException {
        client = new THSpawnClientBuilder()
                .withAddress(new URI("http://localhost:" + port + "/adapter/" + serviceName))
                .withNetworkTimeout(0)
                .build(ProviderProxySrv.Iface.class);
    }

    @Test(expected = WUnavailableResultException.class)
    public void testWhenRulesNotFound() throws TException {
        uploadRulesFromJsonString("[]");
        client.processPayment(buildTBase(PaymentContext.class));
    }

    @Test
    public void testForSuccessResultRules() throws TException {
        uploadRulesFromResource("/cases/success.json");
        assertTrue(client.processPayment(buildTBase(PaymentContext.class)).getIntent().getFinish().getStatus().isSetSuccess());
        assertTrue(client.handlePaymentCallback(ByteBuffer.wrap(new byte[0]), buildTBase(PaymentContext.class)).getResult().getIntent().getFinish().getStatus().isSetSuccess());
        assertTrue(client.generateToken(buildTBase(RecurrentTokenContext.class)).getIntent().getFinish().getStatus().isSetSuccess());
        assertTrue(client.handleRecurrentTokenCallback(ByteBuffer.wrap(new byte[0]), buildTBase(RecurrentTokenContext.class)).getResult().getIntent().getFinish().getStatus().isSetSuccess());
    }

    @Test
    public void testForFailureResultRules() throws TException {
        uploadRulesFromResource("/cases/failure.json");
        Failure failure = TErrorUtil.toGeneral("invalid:invalid:invalid:invalid:invalid:invalid");
        assertEquals(failure, client.processPayment(buildTBase(PaymentContext.class)).getIntent().getFinish().getStatus().getFailure());
        assertEquals(failure, client.handlePaymentCallback(ByteBuffer.wrap(new byte[0]), buildTBase(PaymentContext.class)).getResult().getIntent().getFinish().getStatus().getFailure());
        assertEquals(failure, client.generateToken(buildTBase(RecurrentTokenContext.class)).getIntent().getFinish().getStatus().getFailure());
        assertEquals(failure, client.handleRecurrentTokenCallback(ByteBuffer.wrap(new byte[0]), buildTBase(RecurrentTokenContext.class)).getResult().getIntent().getFinish().getStatus().getFailure());
    }

    @Test
    public void testForSleepResultRules() throws TException {
        uploadRulesFromResource("/cases/sleep.json");
        assertEquals(5000, client.processPayment(buildTBase(PaymentContext.class)).getIntent().getSleep().getTimer().getTimeout());
        assertEquals(5000, client.handlePaymentCallback(ByteBuffer.wrap(new byte[0]), buildTBase(PaymentContext.class)).getResult().getIntent().getSleep().getTimer().getTimeout());
        assertEquals(5000, client.generateToken(buildTBase(RecurrentTokenContext.class)).getIntent().getSleep().getTimer().getTimeout());
        assertEquals(5000, client.handleRecurrentTokenCallback(ByteBuffer.wrap(new byte[0]), buildTBase(RecurrentTokenContext.class)).getResult().getIntent().getSleep().getTimer().getTimeout());
    }

    @Test
    @Ignore
    public void testDeltaWithDelay() throws Exception {
        uploadRulesFromJsonString("[{'request':{'method':'generate_token'},'response':{'delay':{'type':'fixed','value':10},'result':{'intent':{'finish':{'success':{}}}}}}]");
        client.generateToken(buildTBase(RecurrentTokenContext.class));
        IntSummaryStatistics summaryStatistics = IntStream.rangeClosed(1, 1000)
                .parallel()
                .map(
                        i -> {
                            try {
                                long start = System.currentTimeMillis();
                                client.generateToken(buildTBase(RecurrentTokenContext.class));
                                return Math.toIntExact(System.currentTimeMillis() - start);
                            } catch (TException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                )
                .summaryStatistics();
        assertEquals(10.0, summaryStatistics.getAverage(), 10.0);
    }

    @Test(expected = WUnavailableResultException.class)
    public void testWhenUnavailableResult() throws TException {
        uploadRulesFromJsonString("[{'request':{'method':'generate_token'},'response':{'result':{'error':{'unavailable_result':{'reason':'Deadline reached'}}}}}]");
        client.generateToken(buildTBase(RecurrentTokenContext.class));
    }

    @Test(expected = WUndefinedResultException.class)
    public void testWhenUndefinedResult() throws TException {
        uploadRulesFromJsonString("[{'request':{'method':'generate_token'},'response':{'result':{'error':{'undefined_result':{'reason':'Result unexpected'}}}}}]");
        client.generateToken(buildTBase(RecurrentTokenContext.class));
    }

    @Test(expected = WRuntimeException.class)
    public void testWhenUnexpectedError() throws TException {
        uploadRulesFromJsonString("[{'request':{'method':'generate_token'},'response':{'result':{'error':{'unexpected_error':{}}}}}]");
        client.generateToken(buildTBase(RecurrentTokenContext.class));
    }


    public void uploadRulesFromResource(String path) {
        try {
            uploadRulesFromJsonByteArray(ByteStreams.toByteArray(new ClassPathResource(path).getInputStream()));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public void uploadRulesFromJsonString(String jsonString) {
        uploadRulesFromJsonByteArray(jsonString.getBytes());
    }

    public void uploadRulesFromJsonByteArray(byte[] jsonBinary) {
        try {
            requestStubService.updateRules(objectMapper.readValue(jsonBinary, new TypeReference<List<Rule>>() {
            }));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public <T extends TBase> T buildTBase(Class<T> tClass) {
        try {
            return new MockTBaseProcessor(MockMode.ALL, 15, 1).process(tClass.getDeclaredConstructor().newInstance(), new TBaseHandler<>(tClass));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }
}
