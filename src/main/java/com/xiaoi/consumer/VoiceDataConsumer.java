package com.xiaoi.consumer;

import com.alibaba.nls.client.protocol.NlsClient;
import com.xiaoi.rpc.proto.VoiceRequest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author ryan.song
 * @Date 2018/12/5
 **/
@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class VoiceDataConsumer {


    @KafkaListener(topics = {"voice-data"})
    public void receiveData(VoiceRequest voiceRequest){
        System.out.println("SessionId=" + voiceRequest.getSessionId());
        System.out.println("data=" + voiceRequest.getData());
        System.out.println(voiceRequest.getData().toByteArray());
    }


}
