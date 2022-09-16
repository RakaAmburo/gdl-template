package com.template.providers.controller;

import java.time.Duration;
import reactor.core.publisher.Flux;

public class Test2 {

  public static void main(String[] args) {
    Flux.interval(Duration.ofMillis(50))
        .onBackpressureDrop()
        .delayElements(Duration.ofMillis(500))
        .take(33)
        .elapsed()
        .log();
  }

}
