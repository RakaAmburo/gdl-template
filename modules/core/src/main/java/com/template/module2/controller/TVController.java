package com.template.module2.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;

@Controller
public class TVController {

    @Autowired
    private RSocketRequester rSocketRequester;
    private final Many<Long> logs;
    private final Flux<Long> logsFlux;

    public TVController() {
        logs = Sinks.many().multicast().directBestEffort();
        logsFlux = logs.asFlux();
    }

    @MessageMapping("core.process")
    public Flux<BankUser> playMovie(Flux<Integer> userId) {

        Flux<BankUser> bankUser =
            userId.map(id -> BankUser.builder().index(id).build()).doOnNext(user -> {
                System.out.println("enviando a prov " + user.getIndex());
            }).elapsed().doOnNext(it -> {
                System.out.println("I took " + it.getT1() + " MS");
                logs.tryEmitNext(it.getT1());
            })
                .map(Tuple2::getT2);

        Flux<BankUser> users = this.rSocketRequester
            .route("to.providers").data(bankUser).retrieveFlux(BankUser.class)
            .doOnNext(user -> {
                System.out.println("Receibed " + user.getIndex());
            });

        return users;
    }

    @MessageMapping("core.logger")
    public Flux<Log> playMovie() {

        return logsFlux.buffer(Duration.ofMillis(1000))
            .map(list -> {
                return Log.builder().id(1L)
                    .origin("core")
                    .type("rate")
                    .rate(list.size())
                    .build();
            });
    }

}
