package com.template.gateway.controller;

import com.template.model.Log;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
public class GatewayController {

  private final FluxLogger<Long> fluxLogger;

  @Autowired
  public GatewayController(FluxLogger<Long> fluxLogger) {
    this.fluxLogger = fluxLogger;
  }

  @MessageMapping("gateway.logger")
  public Flux<Log> gatewayLogger() {

    return fluxLogger.getFluxLog().buffer(Duration.ofMillis(1000))
        .map(list -> Log.builder().id(1L).origin("gateway").type("rate").rate(list.size()).build());
  }

}
