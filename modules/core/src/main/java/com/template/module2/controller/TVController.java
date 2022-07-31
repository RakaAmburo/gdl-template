package com.template.module2.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import com.template.utils.FluxLogger;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Slf4j
@Controller
public class TVController {

  private final FluxLogger<Log> fluxLogger;
  private final RSocketRequester rSocketRequester;

  @Autowired
  public TVController(RSocketRequester rSocketRequester, FluxLogger<Log> fluxLogger) {
    this.rSocketRequester = rSocketRequester;
    this.fluxLogger = fluxLogger;
  }

  @MessageMapping("core.process")
  public Flux<BankUser> playMovie(Flux<String> userId) {

    Flux<BankUser> bankUser =
        userId.map(id -> BankUser.builder().id(id).build())
            .doOnNext(user -> {
              log.info("enviando a providers, user: " + user.getIndex());
              fluxLogger.emit(Log.builder().build());
            });

    return this.rSocketRequester
        .route("to.providers").data(bankUser).retrieveFlux(BankUser.class)
        .doOnNext(user -> log.info("Receibed " + user.getIndex()));
  }

  @MessageMapping("core.logger")
  public Flux<Log> playMovie() {

    return fluxLogger.getFluxLog().buffer(Duration.ofMillis(1000))
        .map(logs -> Log.builder().id(1L)
            .origin("core")
            .type("rate")
            .rate(logs.size())
            .build());
  }

}
