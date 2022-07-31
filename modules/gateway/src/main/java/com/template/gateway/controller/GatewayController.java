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

    return fluxLogger.getFluxLog().buffer(Duration.ofMillis(1000))
        .map(logs -> Log.builder()
            .id(1L).origin("gateway")
            .type("rate").rate(logs.size()).build());
  }

}
