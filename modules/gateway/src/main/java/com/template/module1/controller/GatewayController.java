package com.template.module1.controller;

import com.template.model.Log;
import java.time.Duration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class GatewayController {

    @MessageMapping("gateway.logger")
    public Flux<Log> playMovie() {

        return Flux.interval(Duration.ofMillis(300L)).map(i -> Log.builder().id(i)
            .origin("gateway")
            .type("rate")
            .rate(i.intValue())
            .build());
    }

}
