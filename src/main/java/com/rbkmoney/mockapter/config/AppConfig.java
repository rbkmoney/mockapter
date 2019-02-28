package com.rbkmoney.mockapter.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Lazy
    @Bean
    public Consul consul(
            @Value("#{'${consul.hosts}'.split(',')}") List<String> hosts,
            @Value("${consul.blacklistTimeInMillis}") long blacklistTimeInMillis
    ) {
        Consul.Builder builder = Consul.builder();
        if (hosts.size() == 1) {
            builder.withHostAndPort(HostAndPort.fromString(hosts.get(0)));
        } else {
            builder.withMultipleHostAndPort(
                    hosts.stream()
                            .map(HostAndPort::fromString)
                            .collect(Collectors.toList()),
                    blacklistTimeInMillis
            );
        }

        return builder.build();
    }

    @Lazy
    @Bean
    public KeyValueClient keyValueClient(Consul consul) {
        return consul.keyValueClient();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
    }

}
