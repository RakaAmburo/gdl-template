package com.template.providers.controller;

import com.template.utils.TimerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleyController {

  private final TimerContainer timerContainer;

  @Autowired
  public DeleyController(TimerContainer timerContainer) {
    this.timerContainer = timerContainer;
  }

  @PostMapping("/speed/{speed}")
  public void speed(@PathVariable Integer speed) {
    timerContainer.getFluxTimer().setDelay(speed);
  }

}
