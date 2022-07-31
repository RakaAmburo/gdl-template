package com.template.logger.controller;

import com.template.model.Log;
import com.template.utils.FluxLogger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

@Controller
public class RSocketController {

  private final FluxLogger<Log> fluxLogger;
  private final RestTemplate restTemplate = new RestTemplate();

  RSocketController(FluxLogger<Log> fluxLogger) {
    this.fluxLogger = fluxLogger;
  }

  @MessageMapping("logger.stream")
  public Flux<Log> responseStream() {

    return fluxLogger.getFluxLog();
  }

  @MessageMapping("logger.start.flow")
  public void startFlow(Boolean bol) {

    restTemplate.getForEntity("http://localhost:8080/start", String.class);

  }

  @MessageMapping("logger.start.logs")
  public void startLogs(Boolean bol) {

    restTemplate.getForEntity("http://localhost:8085/start", String.class);

  }

  @MessageMapping("logger.provider.rate")
  public void startLogs(Integer num) {
    System.out.println("pasa por post");
    restTemplate.postForEntity("http://localhost:8084/speed/" + num, null, String.class);

  }
}
