package com.template.gateway.rclient;

import com.template.model.BankUser;
import com.template.utils.FluxLogger;
import io.rsocket.exceptions.ApplicationErrorException;
import java.nio.channels.ClosedChannelException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

@Slf4j
@RestController
public class RsocketClient {

  private final RSocketRequester rSocketRequester;
  private final FluxLogger<Long> fluxLogger;
  private Disposable subcribed;

  @Autowired
  public RsocketClient(RSocketRequester rSocketRequester, FluxLogger<Long> fluxLogger) {
    this.rSocketRequester = rSocketRequester;
    this.fluxLogger = fluxLogger;
  }

  @GetMapping("/start")
  public void start() {
    startFlow();
  }

  private void startFlow() {
    Flux<Integer> bankUserId = Flux.generate((SynchronousSink<Integer> synchronousSink) ->
        synchronousSink.next(Integer.MIN_VALUE)
    )
        .delayElements(Duration.ofMillis(10))
        .elapsed()
        .doOnNext(it -> fluxLogger.emit(it.getT1()))
        .map(Tuple2::getT2)
        .doOnNext(id -> log.info("Request userId to core: " + id));

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
        .doOnNext(processedUser ->
            log.info("Processed user from core: " + processedUser.getIndex())
        );

    subcribed = userProcessed.subscribe();
  }
}
