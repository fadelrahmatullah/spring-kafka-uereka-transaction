package com.core.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import com.core.app.generator.CoreBeanNameGenerator;
import com.core.app.kafkainstance.KafkaInstance;
import com.core.app.util.ResponseUtil;

@Configuration
@EntityScan("com.core.app.entity")
@ComponentScan(value = { "com.core.app.config", "com.core.app.repository",
						"com.core.app.service", "com.core.app.util",
						"com.core.app.kafkainstance" }, nameGenerator = CoreBeanNameGenerator.class, 
	excludeFilters = {
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = CoreConfig.class),
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = KafkaInstance.class),
				@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ResponseUtil.class)
			})
public class CoreJobConfig {
}
