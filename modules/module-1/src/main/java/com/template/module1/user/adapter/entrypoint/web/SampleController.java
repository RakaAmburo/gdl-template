package com.template.module1.user.adapter.entrypoint.web;

import com.template.model.User;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SampleController {

    final RestTemplate restTemplate = new RestTemplate();

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
