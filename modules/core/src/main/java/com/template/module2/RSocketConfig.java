package com.template.module2;

import io.rsocket.transport.netty.client.TcpClientTransport;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.util.retry.Retry;

@Configuration
public class RSocketConfig {

  @Bean
  public RSocketRequester getRSocketRequester(RSocketRequester.Builder builder) {
    return builder
        .rsocketConnector(rSocketConnector -> rSocketConnector
            .reconnect(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(2))))
        .transport(TcpClientTransport.create(6568));
  }

}
