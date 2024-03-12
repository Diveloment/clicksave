package com.altinntech.clicksave;

import com.altinntech.clicksave.core.CSBootstrap;
import com.altinntech.clicksave.core.DefaultProperties;
import com.altinntech.clicksave.exceptions.ClassCacheNotFoundException;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

@Configuration
@AutoConfiguration
public class CSBootstrapConfig {

    @Bean
    @ConditionalOnMissingBean(CSBootstrap.class)
    public CSBootstrap csBootstrap() throws ClassCacheNotFoundException, SQLException {
        DefaultProperties defaultProperties = DefaultProperties.fromEnvironment();
        return new CSBootstrap(defaultProperties);
    }
}