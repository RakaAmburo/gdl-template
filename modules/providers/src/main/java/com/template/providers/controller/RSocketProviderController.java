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

@Slf4j
@Controller
public class RSocketProviderController {

  private final FluxLogger<Log> fluxLogger;
  private final TimerContainer timerContainer;

  @Autowired
  public RSocketProviderController(FluxLogger<Log> fluxLogger, TimerContainer timerContainer) {
    this.fluxLogger = fluxLogger;
    this.timerContainer = timerContainer;
  }

  @MessageMapping("providers.process")
  public Flux<BankUser> providerProcess(Flux<BankUser> bankUserFlux) {

    Timer timer = timerContainer.createTimer(10);

    //return bankUserFlux
    return timer.delayFlux(bankUserFlux)
        .doOnNext(user -> {
          fluxLogger.emit(Log.builder().type("rate").build());
          fluxLogger.emit(Log.builder().type("logs.received").build());
        })
        .doOnComplete(timer::stop);
  }

  @MessageMapping("provider.logger")
  public Flux<Log> providerLogger() {

    Flux<Log> fluxLoggerParent = fluxLogger.getFluxLog();
    Flux<Log> fluxLoggerRate = fluxLoggerParent.filter(user -> "rate".equals(user.getType()))
        .buffer(Duration.ofMillis(1069))
        .map(list -> Log.builder().id(1L)
            .type("rate")
            .rate(list.size())
            .build());
    Flux<Log> fluxLoggerLogs = fluxLoggerParent.filter(user -> user.getType().startsWith("logs"))
        .buffer(Duration.ofMillis(971))
        .map(list -> {
          //long emitted = list.stream().filter(log -> log.getType().contains("emitted")).count();
          long received = list.stream().filter(log -> log.getType().contains("received")).count();
          return Log.builder().id(1L)
              .type("logs")
              .entry("proccesed & returned: " + received)
              .build();
        });

    return fluxLoggerLogs.mergeWith(fluxLoggerRate).map(log -> {
      log.setOrigin("provider");
      return log;
    });

    /*return fluxLogger.getFluxLog().buffer(Duration.ofMillis(1000))
        .map(list -> Log.builder().id(1L)
            .origin("provider")
            .type("rate")
            .rate(list.size())
            .build());*/
    //.doOnNext(c -> log.info("LLENANDO PROVIDERS LOGS QUEUE"))
    //.onBackpressureDrop(droped -> log.info("DROPPED ELEMENT!!!!!!!!!!!"));
  }

}
