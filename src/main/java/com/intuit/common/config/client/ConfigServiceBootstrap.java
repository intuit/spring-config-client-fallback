package com.intuit.sbg.common.config.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: asachdeva
 * Date: 6/30/15
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */

@Configuration
@EnableConfigurationProperties
@PropertySource(value={"configClient.properties", "file:${spring.cloud.config.fallbackLocation:}/fallback.properties"}, ignoreResourceNotFound = true)
public class ConfigServiceBootstrap {

    public static final String FALLBACK_FILE_NAME = "fallback.properties";

    @Autowired
    private ConfigurableEnvironment environment;

    @Value("${spring.cloud.config.fallbackLocation:}")
    private String fallbackLocation;

    @Bean
    public ConfigClientProperties configClientProperties() {
        ConfigClientProperties client = new ConfigClientProperties(this.environment);
        client.setEnabled(false);
        return client;
    }

    @Bean
    public FallbackableConfigServicePropertySourceLocator fallbackableConfigServicePropertySourceLocator() {
        ConfigClientProperties client = configClientProperties();
        FallbackableConfigServicePropertySourceLocator fallbackConfigServicePropertySourceLocator =  new FallbackableConfigServicePropertySourceLocator(
                client, fallbackLocation);
        return  fallbackConfigServicePropertySourceLocator;
    }

}
