package com.template.utils;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Component
public class FluxLogger<T> {

  private final Many<T> logs;
  private final Flux<T> fluxLog;

  public FluxLogger() {
    logs = Sinks.many().multicast().directBestEffort();
    fluxLog = logs.asFlux();
  }

  public Flux<T> getFluxLog() {
    return fluxLog;
  }

  public void emit(T next) {
    logs.tryEmitNext(next);
  }
}
