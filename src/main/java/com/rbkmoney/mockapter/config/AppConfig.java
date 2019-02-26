package com.rbkmoney.mockapter.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    public Consul consul(
            @Value("#{'${consul.hosts}'.split(',')}") List<String> hosts,
            @Value("${consul.blacklistTimeInMillis}") long blacklistTimeInMillis
    ) {
        return Consul.builder()
                .withMultipleHostAndPort(
                        hosts.stream()
                                .map(host -> HostAndPort.fromString(host))
                                .collect(Collectors.toList()),
                        blacklistTimeInMillis
                )
                .build();
    }

    @Bean
    public KeyValueClient keyValueClient(Consul consul) {
        return consul.keyValueClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

}
