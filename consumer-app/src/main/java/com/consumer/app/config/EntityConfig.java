package com.consumer.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.consumer.app.entity")
public class EntityConfig {
    
}
