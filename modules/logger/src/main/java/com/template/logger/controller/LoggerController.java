package com.template.logger.controller;

import com.template.model.Log;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
public class LoggerController {

  private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
  @Autowired
  @Qualifier("coreLoggerRequester")
  private RSocketRequester coreRequester;
  @Autowired
  @Qualifier("providerLoggerRequester")
  private RSocketRequester providerRequester;

  @Autowired
  @Qualifier("gatewayLoggerRequester")
  private RSocketRequester gatewayRequester;

  @GetMapping("/start")
  public void start() {

    startCoreFunc();

    startProviderFunc();

    startGateFunc();

  }

  private void startCoreFunc() {
    this.coreRequester
        .route("core.logger").retrieveFlux(Log.class)
        .doOnError(error -> {
          if (error instanceof ClosedChannelException) {
            Flux.just(Integer.MIN_VALUE).doOnNext(i -> startCoreFunc())
                .subscribeOn(Schedulers.parallel()).subscribe();
          }
        })
        .doOnNext(n -> {
          RSocketController.logs.tryEmitNext(n);
          System.out.println("Receibed from core: " + n.getId());
        }).subscribe();
  }

  private void startProviderFunc() {
    this.providerRequester
        .route("provider.logger").retrieveFlux(Log.class)
        .doOnError(error -> {
          if (error instanceof ClosedChannelException) {
            Flux.just(Integer.MIN_VALUE).doOnNext(i -> startProviderFunc())
                .subscribeOn(Schedulers.parallel()).subscribe();
          }
        })
        .doOnNext(n -> {
          RSocketController.logs.tryEmitNext(n);
          System.out.println("Receibed from provider: " + n.getId());
        }).subscribe();
  }

  private void startGateFunc() {
    this.gatewayRequester
        .route("gateway.logger").retrieveFlux(Log.class)
        .doOnError(error -> {
          if (error instanceof ClosedChannelException) {
            Flux.just(Integer.MIN_VALUE).doOnNext(i -> startGateFunc())
                .subscribeOn(Schedulers.parallel()).subscribe();
          }
        })
        .doOnNext(n -> {
          RSocketController.logs.tryEmitNext(n);
          System.out.println("Receibed from gateway: " + n.getId());
        }).subscribe();
  }

}
