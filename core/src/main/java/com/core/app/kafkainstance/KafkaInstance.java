package com.core.app.kafkainstance;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.serializer.JsonSerializer;

import lombok.extern.slf4j.Slf4j;

@EnableKafka
@Slf4j
@Component
public class KafkaInstance {
    
    @Value("${kafka.bootstrapAddress:#{null}}")
	private String bootstrapAddress;

	@Value("${kafka.group.id:#{null}}")
	private String groupId;

	// @Autowired
	// private IConsumer iConsumer;

    ExecutorService executorService = Executors.newFixedThreadPool(8);

	private Properties loadKafkaConsumerProperties() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "300000");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);

		return props;
	}

    public void buildConsumer(String topic, Class<?> clazz, ApplicationContext context) {
		Properties consumerProperties = loadKafkaConsumerProperties();
		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerProperties);
		kafkaConsumer.subscribe(Arrays.asList(topic));
		try {

			while (true) {

				ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
				if (records.count() == 0) {
					continue;
				}
                
				log.info("Total No. of records received : {}", records.count());
				final CountDownLatch latch = new CountDownLatch(records.count());
                records.forEach(record -> {
					log.info("Consume msg : {}", record.value());
					Object obj = context.getBean(clazz);
					IConsumer iconsumer = (IConsumer) obj;
					executorService.execute(new IConsumerRunnable(latch, iconsumer, record.value()));
                });
				latch.await();
				kafkaConsumer.commitAsync();
				
			}

		} catch (Exception ex) {
			log.error(ex.getMessage());
			kafkaConsumer.close();
		} finally {
            kafkaConsumer.close();
            executorService.shutdown(); 
        }

	}

    private Properties loadKafkaProducerProperties() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return props;
	}

    public void produce(String topic, String message) {
		Properties producerProperties = loadKafkaProducerProperties();
		Producer<String, String> producer = new KafkaProducer<>(producerProperties);
		try {
			producer.send(new ProducerRecord<String, String>(topic, UUID.randomUUID().toString(), message)).get();
		} catch (Exception ex) {
			log.error("Unable to send message=[" + message + "] due to : " + ex.getMessage());
		} finally {
			producer.close();
		}

	}
}
