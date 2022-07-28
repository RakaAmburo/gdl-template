package com.template.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TimerContainer {

  private Timer fluxTimer;

  public Timer createTimer(Integer time) {
    fluxTimer = Timer.of(time);
    return fluxTimer;
  }

}
