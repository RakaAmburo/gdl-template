package com.template.module2;

import com.template.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtherController {

    @GetMapping("/module2/test")
    public User controller(){

        return User.builder().id(1).name("pepe").build();
    }
}
