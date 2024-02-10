package com.consumer.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.consumer.app.consumer.TransactionConsumer;
import com.core.app.kafkainstance.KafkaInstance;

import lombok.extern.slf4j.Slf4j;

@EnableDiscoveryClient
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"com.consumer.app"})
@Slf4j
@EnableScheduling
public class ConsumerApplication  implements ApplicationRunner{

	@Autowired
	private KafkaInstance kafkaInstance;

	@Value("${kafka.topic.name:#{null}}")
	private String topic;

	@Autowired
	private ApplicationContext applicationContext;

	public static void main(String[] args) {
		SpringApplication.run(ConsumerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try {
			kafkaInstance.buildConsumer(topic, TransactionConsumer.class, applicationContext);
		} catch (Exception e) {
			log.info("ERROR: " + e.getMessage());
		}
	}

}
