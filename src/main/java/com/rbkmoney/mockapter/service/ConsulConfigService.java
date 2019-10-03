package com.rbkmoney.mockapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.cache.KVCache;
import com.rbkmoney.mockapter.model.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(value = "consul.enabled", havingValue = "true")
public class ConsulConfigService {

    private final KeyValueClient keyValueClient;

    private final RequestStubService requestStubService;

    private final ObjectMapper objectMapper;

    private final String keyPath;

    private final Resource defaultConfigResource;

    private final KVCache cache;


    public ConsulConfigService(
            KeyValueClient keyValueClient,
            RequestStubService requestStubService,
            ObjectMapper objectMapper,
            @Value("${consul.keyPath}") String keyPath,
            @Value("${configuration.resource}") Resource defaultConfigResource
    ) {
        this.keyValueClient = keyValueClient;
        this.requestStubService = requestStubService;
        this.objectMapper = objectMapper;
        this.keyPath = keyPath;
        this.defaultConfigResource = defaultConfigResource;
        this.cache = KVCache.newCache(keyValueClient, keyPath);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        cache.addListener(
                newValues -> newValues
                        .values()
                        .stream()
                        .filter(value -> value.getKey().equals(keyPath))
                        .findFirst()
                        .ifPresent(value -> value.getValueAsString().ifPresent(this::uploadRules))
        );
        initializeRulesIfNotExists();
        cache.start();
    }

    public void uploadRules(String rulesJson) {
        try {
            log.info("Trying to apply new rules, rulesJson='{}'", rulesJson);
            requestStubService.updateRules(objectMapper.readValue(rulesJson, new TypeReference<List<Rule>>() {
            }));
            log.info("New rules have been applied, rulesJson='{}'", rulesJson);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private void initializeRulesIfNotExists() {
        keyValueClient.getValue(keyPath).ifPresentOrElse(
                rules -> log.info("Configuration already present on key '{}'", rules.getKey()),
                () -> {
                    try {
                        log.info("Trying to upload default configuration from path '{}'", defaultConfigResource.getURL());
                        JsonNode jsonNode = objectMapper.readTree(defaultConfigResource.getInputStream());
                        keyValueClient.putValue(keyPath, objectMapper.writeValueAsString(jsonNode));
                        log.info("Configuration have been uploaded, rules='{}'", jsonNode);
                    } catch (IOException ex) {
                        throw new UncheckedIOException(ex);
                    }
                }
        );
    }

    @PreDestroy
    public void stop() {
        cache.stop();
    }

}
