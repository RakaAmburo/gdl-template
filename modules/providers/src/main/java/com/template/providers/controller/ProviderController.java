package com.template.providers.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import com.template.utils.Timer;
import java.time.Duration;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class ProviderController {

    public static Timer timer;

    @MessageMapping("to.providers")
    public Flux<BankUser> playMovie(Flux<BankUser> bankUserFlux) {

        timer = Timer.of(10);


        return timer.delayFlux(bankUserFlux).doOnComplete(()->{
            timer.stop();
        });
    }

    @MessageMapping("provider.logger")
    public Flux<Log> playMovie() {

        return Flux.interval(Duration.ofMillis(300L)).map(i -> Log.builder().id(i)
            .origin("provider")
            .type("rate")
            .rate(i.intValue())
            .build());
    }

}
