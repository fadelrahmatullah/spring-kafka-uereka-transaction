package com.app.produce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(com.core.app.config.CoreConfig.class)
public class CoreConfig {

}
