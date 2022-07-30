package com.template.providers.controller;

import com.template.model.BankUser;
import com.template.model.Log;
import com.template.utils.FluxLogger;
import com.template.utils.Timer;
import com.template.utils.TimerContainer;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

@Slf4j
@Controller
public class ProviderController {

  private final FluxLogger<Long> fluxLogger;
  private final TimerContainer timerContainer;

  @Autowired
  public ProviderController(FluxLogger<Long> fluxLogger, TimerContainer timerContainer) {
    this.fluxLogger = fluxLogger;
    this.timerContainer = timerContainer;
  }

  @MessageMapping("to.providers")
  public Flux<BankUser> playMovie(Flux<BankUser> bankUserFlux) {

    Timer timer = timerContainer.createTimer(10);

    return timer.delayFlux(bankUserFlux)
        .elapsed().doOnNext(it -> fluxLogger.emit(it.getT1()))
        .map(Tuple2::getT2)
        .doOnComplete(timer::stop);
  }

  @MessageMapping("provider.logger")
  public Flux<Log> playMovie() {

    return fluxLogger.getFluxLog().buffer(Duration.ofMillis(1000))
        .map(list -> Log.builder().id(1L)
            .origin("provider")
            .type("rate")
            .rate(list.size())
            .build())
        .doOnNext(c -> log.info("LLENANDO PROVIDERS LOGS QUEUE"))
        .onBackpressureDrop(droped -> log.info("DROPPED ELEMENT!!!!!!!!!!!"));
  }

}
