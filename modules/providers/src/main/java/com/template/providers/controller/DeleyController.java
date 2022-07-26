package com.template.providers.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeleyController {

    @PostMapping("/speed/{speed}")
    public void speed(@PathVariable Integer speed) {
        ProviderController.timer.setDelay(speed);
    }


}
