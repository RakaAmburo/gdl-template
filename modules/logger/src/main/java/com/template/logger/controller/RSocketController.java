package com.template.logger.controller;

import com.template.model.Log;
import io.rsocket.Payload;
import java.time.Duration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Controller
public class RSocketController {

    public static Many<Log> logs = Sinks.many().multicast().directBestEffort();
    private RestTemplate restTemplate = new RestTemplate();

    @MessageMapping("logger.stream")
    public Flux<Log> responseStream() {

        return logs.asFlux();
    }

    @MessageMapping("logger.start.flow")
    public void startFlow(Boolean bol) {

        restTemplate.getForEntity("http://localhost:8080/start", String.class);

    }

    @MessageMapping("logger.start.logs")
    public void startLogs(Boolean bol) {

        restTemplate.getForEntity("http://localhost:8085/start", String.class);

    }

    @MessageMapping("logger.provider.rate")
    public void startLogs(Integer num) {
        System.out.println("pasa por post");
        restTemplate.postForEntity("http://localhost:8084/speed/" + num, null, String.class);

    }
}
