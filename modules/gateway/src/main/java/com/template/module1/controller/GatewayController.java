package com.template.module1.controller;

import com.template.model.Log;
import java.time.Duration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Controller
public class GatewayController {

    public GatewayController(){
        logs = Sinks.many().multicast().directBestEffort();
        logsFlux = logs.asFlux();
    }

    public static Many<Long> logs;
    private static Flux<Long> logsFlux;

    /**
     * Adding javadoc
     * @return
     */
    @MessageMapping("gateway.logger")
    public Flux<Log> playMovie() {

        return logsFlux.buffer(Duration.ofMillis(1000))
            .map(list -> {
                return Log.builder().id(1L)
                    .origin("gateway")
                    .type("rate")
                    .rate(list.size())
                    .build();
            });
    }

}
