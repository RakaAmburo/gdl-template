package com.template.logger.controller;

import com.template.model.Log;
import com.template.utils.FluxLogger;
import java.nio.channels.ClosedChannelException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
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

  @Autowired
  private FluxLogger<Log> fluxLogger;

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
        .doOnNext(logObj -> {
          fluxLogger.emit(logObj);
          log.info("Receibed from core: " + logObj.getId());
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
        .doOnNext(logObj -> {
          fluxLogger.emit(logObj);
          log.info("Receibed from provider: " + logObj.getId());
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
        .doOnNext(logObj -> {
          fluxLogger.emit(logObj);
          log.info("Receibed from gateway: " + logObj.getId());
        }).subscribe();
  }

}
