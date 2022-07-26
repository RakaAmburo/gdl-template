package com.template.gateway.user.domain;

import com.template.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

@RestController
public class SampleController {

    RestTemplate restTemplate = new RestTemplate();

    @Value("${module2.url}")
    private String url;

    @GetMapping("/test")
    public User controller() {

        User result = restTemplate.getForObject(url, User.class);
        assert result != null;
        result.setTime(new Date().toString());
        return result;
    }

}
