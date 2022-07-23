package com.template.module2;

import io.rsocket.transport.netty.client.TcpClientTransport;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.util.retry.Retry;

@Configuration
public class RSocketConfig {

    /*@Bean
    public RSocketStrategies rSocketStrategies() {
        return RSocketStrategies.builder()
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                .build();
    }*/

    @Bean
    public RSocketRequester getRSocketRequester(RSocketRequester.Builder builder){
        return builder
                .rsocketConnector(rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                //.dataMimeType(MediaType.APPLICATION_CBOR)

                .transport(TcpClientTransport.create(6568));
    }

}
