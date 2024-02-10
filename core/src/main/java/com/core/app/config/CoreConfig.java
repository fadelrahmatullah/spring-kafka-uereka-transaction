package com.core.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.core.app.generator.CoreBeanNameGenerator;

@Configuration
@EntityScan("com.core.app.entity")
@ComponentScan(value = "com.core.app", nameGenerator = CoreBeanNameGenerator.class, excludeFilters =
        {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CoreJobConfig.class)})
public class CoreConfig {
}
