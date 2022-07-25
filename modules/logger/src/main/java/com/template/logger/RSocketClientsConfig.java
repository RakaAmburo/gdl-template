package com.template.logger;

import io.rsocket.transport.netty.client.TcpClientTransport;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Configuration
public class RSocketClientsConfig {

    @Bean(name = "coreLoggerRequester")
    public RSocketRequester getRSocketCoreLoggerRequester(RSocketRequester.Builder builder) {
        return builder
            .rsocketConnector(
                rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(2))))
            .transport(TcpClientTransport.create(6565));
    }

    @Bean(name = "providerLoggerRequester")
    public RSocketRequester getRSocketProviderLoggerRequester(RSocketRequester.Builder builder) {
        return builder
            .rsocketConnector(
                rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(2))))
            .transport(TcpClientTransport.create(6568));
    }

    @Bean(name = "gatewayLoggerRequester")
    public RSocketRequester getRSocketGatewayLoggerRequester(RSocketRequester.Builder builder) {
        return builder
            .rsocketConnector(rSocketConnector -> rSocketConnector.reconnect(Retry.fixedDelay(Integer.MAX_VALUE, Duration.ofSeconds(2))
                .doBeforeRetry(c -> {
                    //System.out.println("trying to reconect");
                })))
            .transport(TcpClientTransport.create(6566)) ;
    }

}
