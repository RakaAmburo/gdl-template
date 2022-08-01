package com.template.gateway.controller;

import com.template.model.Log;
import com.template.utils.FluxLogger;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class GatewayController {

  private final FluxLogger<Log> fluxLogger;

  @Autowired
  public GatewayController(FluxLogger<Log> fluxLogger) {
    this.fluxLogger = fluxLogger;
  }

  @MessageMapping("gateway.logger")
  public Flux<Log> gatewayLogger() {

    Flux<Log> fluxLoggerParent = fluxLogger.getFluxLog();
    Flux<Log> fluxLoggerRate = fluxLoggerParent.filter(user -> "rate".equals(user.getType()))
        .buffer(Duration.ofMillis(1000))
        .map(list -> Log.builder().id(1L)
            .type("rate")
            .rate(list.size())
            .build());
    Flux<Log> fluxLoggerLogs = fluxLoggerParent.filter(user -> user.getType().equals("logs"));

    return fluxLoggerLogs.mergeWith(fluxLoggerRate).map(log -> {
      log.setOrigin("gateway");
      return log;
    });
  }

}
