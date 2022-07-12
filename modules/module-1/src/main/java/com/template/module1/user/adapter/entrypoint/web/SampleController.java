package com.template.module1.user.adapter.entrypoint.web;

import com.github.javafaker.Faker;
import com.template.model.User;
import java.time.Duration;
import java.util.Date;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
public class SampleController {

    final RestTemplate restTemplate = new RestTemplate();
    private static final Faker FAKER = new Faker();

    @Value("${module2.url}")
    private String url;

    @GetMapping("/test")
    public User controller() {

        User result = restTemplate.getForObject(url, User.class);
        assert result != null;
        result.setTime(new Date().toString());
        return result;
    }

    @GetMapping(value = "flux", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<User> getInt() {
        return Flux.fromStream(IntStream.range(0, 10).mapToObj(
            i -> User.builder().id(i).name(FAKER.name().firstName()).time(FAKER.date().birthday().toString()).build()))
            .delayElements(Duration.ofMillis(300));

    }

    @GetMapping(value = "activate", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<User> activateFlux(){

        WebClient client = WebClient.create("http://localhost:8080");
        return client.get().uri("flux").retrieve().bodyToFlux(User.class);
    }

}
