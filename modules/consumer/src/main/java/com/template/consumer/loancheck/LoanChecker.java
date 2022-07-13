package com.template.consumer.loancheck;

import com.template.loan.model.Loan;
import com.template.loan.model.Statuses;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.Many;

@Component
public class LoanChecker {

    public static final Logger log = LoggerFactory.getLogger(LoanChecker.class);
    private static final Long MAX_AMOUNT = 10000L;
    private final Many<Loan> sink = Sinks.many().unicast().onBackpressureError();
    private final Flux<Loan> hotFlux = sink.asFlux();

    @Autowired
    private StreamBridge streamBridge;

    @Bean
    public Consumer<Loan> loanConsumer() {
        return loan -> {
            log.info("Consumer Received : " + loan.getName());
            if (loan.getAmount() > MAX_AMOUNT) {
                loan.setStatus(Statuses.DECLINED.name());
                streamBridge.send("loanDeclined-out-0", loan);
            } else {
                loan.setStatus(Statuses.APPROVED.name());
                sink.tryEmitNext(loan);
            }

        };
    }

    @Bean
    public Supplier<Flux<Loan>> loanProcessed() {
        return () -> hotFlux;
    }

    private static <T> Message<T> message(T val) {
        return MessageBuilder.withPayload(val).build();
    }
}
