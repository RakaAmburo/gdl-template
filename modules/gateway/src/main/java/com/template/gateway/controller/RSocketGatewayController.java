package com.template.gateway.controller;

import com.template.model.Log;
import com.template.utils.FluxLogger;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Controller
public class RSocketGatewayController {

  private final FluxLogger<Log> fluxLogger;

  @Autowired
  public RSocketGatewayController(FluxLogger<Log> fluxLogger) {
    this.fluxLogger = fluxLogger;
  }

  @MessageMapping("gateway.logger")
  public Flux<Log> gatewayLogger() {

    Flux<Log> fluxLoggerParent = fluxLogger.getFluxLog();
    Flux<Log> fluxLoggerRate = fluxLoggerParent.filter(user -> "rate".equals(user.getType()))
        .buffer(Duration.ofMillis(1025))
        .map(list -> Log.builder().id(1L)
            .type("rate")
            .rate(list.size())
            .build()).subscribeOn(Schedulers.parallel());
    Flux<Log> fluxLoggerLogs = fluxLoggerParent.filter(user -> user.getType().startsWith("logs"))
        .buffer(Duration.ofMillis(1007))
        .map(list -> {
          long emitted = list.stream().filter(log -> log.getType().contains("emitted")).count();
          long received = list.stream().filter(log -> log.getType().contains("received")).count();
          return Log.builder().id(1L)
              .type("logs")
              .entry("emitted: " + emitted + ", received: " + received)
              .build();
        });

    return fluxLoggerLogs.mergeWith(fluxLoggerRate).map(log -> {
      log.setOrigin("gateway");
      return log;
    });
  }

}
