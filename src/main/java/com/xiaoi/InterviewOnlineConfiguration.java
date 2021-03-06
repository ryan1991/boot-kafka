package com.xiaoi;

import com.xiaoi.rpc.client.VoiceRequestDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author ryan.song
 * @Date 2018/12/5
 **/

@Configuration
@EnableKafka
public class InterviewOnlineConfiguration {



    @Value("${kafka.consumer.bootstrapServers}")
    private String consumerBootstrapServers;

    @Value("${kafka.consumer.groupId}")
    private String consumerGroupId;

    @Value("${kafka.consumer.enableAutoCommit}")
    private String consumerEnableAutoCommit;

    @Value("${kafka.consumer.autoCommitIntervalMs}")
    private String consumerAutoCommitIntervalMs;

    @Value("${kafka.consumer.sessionTimeoutMs}")
    private String consumerSessionTimeoutMs;

    @Value("${kafka.consumer.maxPollRecords}")
    private String consumerMaxPollRecords;

    @Value("${kafka.consumer.autoOffsetReset}")
    private String consumerAutoOffsetReset;





    /**
     * ConsumerFactory
     * @return
     */
    @Bean
    public ConsumerFactory<Object, Object> dataConsumerFactory(){
        Map<String, Object> configs = new HashMap<>(); //参数
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerBootstrapServers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
//        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, "test-consumer-group");
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, consumerEnableAutoCommit);
        configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, consumerAutoCommitIntervalMs);
        configs.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, consumerSessionTimeoutMs);
        configs.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, consumerMaxPollRecords); //批量消费数量
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerAutoOffsetReset);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,VoiceRequestDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(configs);
    }


    /**
     * 添加KafkaListenerContainerFactory，用于批量消费消息
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<?> batchContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<Object, Object> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(dataConsumerFactory());
        containerFactory.setConcurrency(4);
        containerFactory.setBatchListener(true); //批量消费
        containerFactory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);

        return containerFactory;
    }

}
