package com.template.providers.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import com.template.utils.Timer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.util.function.Tuple2;

import java.time.Duration;

@Controller
public class ProviderController {

    public static Timer timer;

    private final Many<Long> logs;
    private final Flux<Long> logsFlux;

    public ProviderController() {
        logs = Sinks.many().multicast().directBestEffort();
        logsFlux = logs.asFlux();
    }

    @MessageMapping("to.providers")
    public Flux<BankUser> playMovie(Flux<BankUser> bankUserFlux) {

        timer = Timer.of(10);

        return timer.delayFlux(bankUserFlux)

                .elapsed().doOnNext(it -> {

                    logs.tryEmitNext(it.getT1());
                })
                .map(Tuple2::getT2)
                .doOnComplete(() -> {
                    timer.stop();
                });
    }

    @MessageMapping("provider.logger")
    public Flux<Log> playMovie() {

        return logsFlux.buffer(Duration.ofMillis(1000))
                .map(list -> {
                    return Log.builder().id(1L)
                            .origin("provider")
                            .type("rate")
                            .rate(list.size())
                            .build();
                })
                .doOnNext(c -> {
                    System.out.println("LLENANDO PROVIDERS LOGS QUEUE");
                })
                .onBackpressureDrop(droped -> {
                    System.out.println("DROPPED ELEMENT!!!!!!!!!!!");
                });
    }

}
