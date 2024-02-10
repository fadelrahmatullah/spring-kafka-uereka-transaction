package com.app.produce.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.app.produce.entity")
public class EntityConfig {
    
}
