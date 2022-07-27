package com.template.module2.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import com.template.utils.FluxLogger;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

@Controller
public class TVController {

  private final FluxLogger<Long> fluxLogger;
  private final RSocketRequester rSocketRequester;

  @Autowired
  public TVController(RSocketRequester rSocketRequester, FluxLogger<Long> fluxLogger) {
    this.rSocketRequester = rSocketRequester;
    this.fluxLogger = fluxLogger;
  }

  @MessageMapping("core.process")
  public Flux<BankUser> playMovie(Flux<Integer> userId) {

    Flux<BankUser> bankUser =
        userId.map(id -> BankUser.builder().index(id).build()).doOnNext(user -> {
          System.out.println("enviando a prov " + user.getIndex());
        }).elapsed().doOnNext(it -> {
          System.out.println("I took " + it.getT1() + " MS");
          fluxLogger.emit(it.getT1());
        })
            .map(Tuple2::getT2);

    return this.rSocketRequester
        .route("to.providers").data(bankUser).retrieveFlux(BankUser.class)
        .doOnNext(user -> {
          System.out.println("Receibed " + user.getIndex());
        });
  }

  @MessageMapping("core.logger")
  public Flux<Log> playMovie() {

    return fluxLogger.getFluxLog().buffer(Duration.ofMillis(1000))
        .map(list -> Log.builder().id(1L)
            .origin("core")
            .type("rate")
            .rate(list.size())
            .build());
  }

}
