package com.template.logger.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import java.nio.channels.ClosedChannelException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.retry.Retry;

@RestController
public class LoggerController {

    @Autowired
    @Qualifier("coreLoggerRequester")
    private RSocketRequester coreRequester;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    @Qualifier("providerLoggerRequester")
    private RSocketRequester providerRequester;

    @Autowired
    @Qualifier("gatewayLoggerRequester")
    private RSocketRequester gatewayRequester;

    @GetMapping("/start")
    public void start() {

        this.coreRequester
            .route("core.logger").retrieveFlux(Log.class)
            .doOnNext(n -> {
                RSocketController.logs.tryEmitNext(n);
                System.out.println("Receibed from core: " + n.getId());
            }).subscribe();

        this.providerRequester
            .route("provider.logger").retrieveFlux(Log.class)
            .doOnNext(n -> {
                RSocketController.logs.tryEmitNext(n);
                System.out.println("Receibed from provider: " + n.getId());
            }).subscribe();

        startGateFunc();

    }

    private void startGateFunc() {
        this.gatewayRequester
            .route("gateway.logger").retrieveFlux(Log.class)
            .doOnError(error -> {
                if (error instanceof ClosedChannelException) {
                    Flux.just(Integer.MIN_VALUE).doOnNext(i -> {
                        startGateFunc();
                    }).subscribeOn(Schedulers.parallel()).subscribe();
                }
            })
            .doOnNext(n -> {
                RSocketController.logs.tryEmitNext(n);
                System.out.println("Receibed from gateway: " + n.getId());
            }).subscribe();
    }

}
