package com.template.gateway.rclient;

import com.template.model.BankUser;
import com.template.model.Log;
import com.template.utils.FluxLogger;
import io.rsocket.exceptions.ApplicationErrorException;
import java.nio.channels.ClosedChannelException;
import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
public class RsocketClient {

  private final RSocketRequester rSocketRequester;
  private final FluxLogger<Log> fluxLogger;
  private Disposable subcribed;

  @Autowired
  public RsocketClient(RSocketRequester rSocketRequester, FluxLogger<Log> fluxLogger) {
    this.rSocketRequester = rSocketRequester;
    this.fluxLogger = fluxLogger;
  }

  @GetMapping("/start")
  public void start() {
    startFlow();
  }

  private void startFlow() {
    Flux<String> bankUserId = Flux.generate((SynchronousSink<Integer> synchronousSink) ->
        synchronousSink.next(Integer.MIN_VALUE)
    ).map(oldId -> UUID.randomUUID().toString())
        .delayElements(Duration.ofMillis(10))
        .doOnNext(id -> {
          fluxLogger.emit(Log.builder().type("rate").build());
          fluxLogger.emit(Log.builder().type("logs.emitted").build());
          //log.info("Request userId to core: " + id);
        });

    Flux<BankUser> userProcessed = this.rSocketRequester
        .route("core.process").data(bankUserId)
        .retrieveFlux(BankUser.class)
        .doOnError(error -> {
          if (error instanceof ClosedChannelException
              || error instanceof ApplicationErrorException) {
            Flux.just(Integer.MIN_VALUE).doOnNext(i -> {
              subcribed.dispose();
              startFlow();
            }).subscribeOn(Schedulers.parallel()).subscribe();
          }
        })
        .doOnNext(processedUser -> {
              fluxLogger.emit(Log.builder().type("logs.received").build());
              //log.info("Processed user from core: " + processedUser.getIndex());
            }
        );

    subcribed = userProcessed.subscribe();
  }
}
