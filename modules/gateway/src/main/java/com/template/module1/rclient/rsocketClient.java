package com.template.module1.rclient;

import com.template.model.BankUser;
import java.nio.channels.ClosedChannelException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@RestController
public class rsocketClient {

    @Autowired
    private RSocketRequester rSocketRequester;
    private Disposable subcribed;

    @GetMapping("/start")
    public void start() {
        startFlow();
    }

    private void startFlow(){
        Flux<Integer> bankUserId = Flux.generate((SynchronousSink<Integer> synchronousSink) -> {
            synchronousSink.next(1);
        })
            //Flux<Integer> bankUserId = Flux.range(1, 10000)
            .delayElements(Duration.ofMillis(10))
            .elapsed()
            //.doOnNext(it -> System.out.println("I took " + it.getT1() + " MS"))
            .map(Tuple2::getT2)
            .doOnNext(id -> System.out.println("Requested user: " + id));

        Flux<BankUser> userProcessed = this.rSocketRequester
            .route("core.process").data(bankUserId)
            .retrieveFlux(BankUser.class)
            .doOnError(error -> {
                if (error instanceof ClosedChannelException) {
                    Flux.just(Integer.MIN_VALUE).doOnNext(i -> {
                        subcribed.dispose();
                        startFlow();
                    }).subscribeOn(Schedulers.parallel()).subscribe();
                }
            })
            .doOnNext(bu -> {
                System.out.println("processed " + bu.getIndex());
            });

        subcribed = userProcessed.subscribe();
    }
}
