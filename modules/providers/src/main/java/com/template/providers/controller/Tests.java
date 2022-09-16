package com.template.providers.controller;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Tests {


  public static void main(String[] args) throws InterruptedException {

    Flux<Object> fluxAsyncBackp = Flux.create(emitter -> {

      // Publish 1000 numbers
      for (int i = 0; i < 1000; i++) {
        System.out.println(Thread.currentThread().getName() + " | Publishing = " + i);
        emitter.next(Entity.builder().id(i).name("Pablo").surName("paparini"));
      }
      // When all values or emitted, call complete.
      emitter.complete();
    })

        .onBackpressureDrop(
            i -> System.out.println(Thread.currentThread().getName() + " | DROPPED = " + i));

    fluxAsyncBackp.subscribeOn(Schedulers.parallel()).publishOn(Schedulers.parallel())
        .subscribe(i -> {
          // Process received value.
          System.out.println(Thread.currentThread().getName() + " | Received = " + i);
          // 500 mills delay to simulate slow subscriber
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        });
    Thread.sleep(3000);
  }
}

@Data
@Builder
class Entity {

  Integer id;
  String name;
  String surName;
}
