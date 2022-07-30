package com.template.utils;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.util.function.Tuple2;

public class Timer {

  private final AtomicBoolean timerActivated = new AtomicBoolean(true);
  private final Flux<Integer> fluxTimer;
  private final Many<Integer> sink;
  private Integer delay = 10;

  private Timer() {
    sink = Sinks.many().multicast().onBackpressureBuffer();
    fluxTimer = sink.asFlux();
    fluxTimer.switchMap(time -> Flux.just(Integer.MIN_VALUE).delayElements(Duration.ofMillis(time)))
        .
            subscribe(val -> {
              if (timerActivated.get()) {
                sink.tryEmitNext(delay);
              }
            });
  }

  public static Timer of(Integer delay) {
    Timer t = new Timer();
    t.start(delay);
    return t;
  }

  public static void main(String[] args) throws InterruptedException {
    Timer t = new Timer();
    t.start(1000);
    Flux<Integer> nums = Flux.just(1, 2, 3, 4, 5, 6);
    t.delayFlux(nums)
        .doOnComplete(t::stop)
        .subscribe(System.out::println);

    Thread.sleep(4000);
    t.setDelay(10);
    Thread.sleep(8000);
  }

  public void setDelay(Integer delay) {
    this.delay = delay;
  }

  public void stop() {
    timerActivated.set(false);
  }

  public void start(Integer time) {
    setDelay(time);
    sink.tryEmitNext(time);
  }

  public <T> Flux<T> delayFlux(Flux<T> flux) {
    return flux.zipWith(fluxTimer).map(Tuple2::getT1);
  }
}
