package com.rbkmoney.mockapter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.rbkmoney.mockapter.model.Rule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;

@Slf4j
@Service
@ConditionalOnProperty(value = "consul.enabled", havingValue = "false")
public class FileConfigService {

    private final RequestStubService requestStubService;
    private final ObjectMapper objectMapper;
    private final Resource defaultConfigResource;


    public FileConfigService(
            RequestStubService requestStubService,
            ObjectMapper objectMapper,
            @Value("${consul.configuration}") Resource defaultConfigResource
    ) {
        this.requestStubService = requestStubService;
        this.objectMapper = objectMapper;
        this.defaultConfigResource = defaultConfigResource;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        try (Reader reader = new InputStreamReader(defaultConfigResource.getInputStream(), Charsets.UTF_8)) {
            String rulesJson = FileCopyUtils.copyToString(reader);
            log.info("Trying to apply new rules, rulesJson='{}'", rulesJson);
            requestStubService.updateRules(objectMapper.readValue(rulesJson, new TypeReference<List<Rule>>() {
            }));
            log.info("New rules have been applied, rulesJson='{}'", rulesJson);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
