package com.group19.orderprocessingservice.services.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;



@Service
public class MessagingService {

    private RedisTemplate<String, Object> redisTemplate;
    private ChannelTopic channelTopic;

    public MessagingService(RedisTemplate<String, Object> redisTemplate, ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }
    
    public void saveMessage(String message) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }



}
