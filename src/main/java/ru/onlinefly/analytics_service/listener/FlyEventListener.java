package ru.onlinefly.analytics_service.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.onlinefly.analytics_service.dto.event.FlyEvent;
import ru.onlinefly.analytics_service.service.ResultService;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlyEventListener {

    private final ResultService resultService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.data.kafka.channels.fly-channel.name}", groupId = "${spring.data.kafka.group-id}")
    public void listen(String event) {
        try {
            FlyEvent flyEvent = objectMapper.readValue(event, FlyEvent.class);
            log.info("FlyEvent received: {}", flyEvent);
            resultService.handleFlyEvent(flyEvent);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }
}
