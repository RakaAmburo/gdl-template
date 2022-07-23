package com.template.utils;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;
import reactor.util.function.Tuple2;

public class Timer {

    public static Timer of(Integer delay){
        Timer t = new Timer();
        t.start(delay);
        return t;
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

    private Integer delay = 10;
    private final AtomicBoolean timerActivated = new AtomicBoolean(true);
    private final Flux<Integer> timer;
    private final Many<Integer> sink;

    private Timer() {

        sink = Sinks.many().multicast().onBackpressureBuffer();
        timer = sink.asFlux();
        timer.switchMap(time -> Flux.just(Integer.MIN_VALUE).delayElements(Duration.ofMillis(time))).
            subscribe(val -> {
                System.out.println("TICK...TOCK");
                if (timerActivated.get()) {
                    sink.tryEmitNext(delay);
                }
            });

    }

    public <T> Flux<T> delayFlux(Flux<T> flux) {
        return flux.zipWith(timer).map(Tuple2::getT1);
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
}
