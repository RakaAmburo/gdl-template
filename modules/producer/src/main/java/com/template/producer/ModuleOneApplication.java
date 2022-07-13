package com.template.producer;

import com.template.loan.model.Loan;
import com.template.producer.loansource.ResultProcessor;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;

@SpringBootApplication
@EnableBinding({ResultProcessor.class})
public class ModuleOneApplication {

    private static final Logger log = LoggerFactory.getLogger(ModuleOneApplication.class);
    private List<String> names = Arrays
        .asList("Donald", "Theresa", "Vladimir", "Angela", "Emmanuel", "Shinzō", "Jacinda", "Kim");
    private List<Long> amounts = Arrays
        .asList(10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 100000000L);

    public static void main(String[] args) {
        SpringApplication.run(ModuleOneApplication.class, args);
    }

    @Bean
    @InboundChannelAdapter(value = ResultProcessor.APPLICATIONS_OUT, poller = @Poller(fixedDelay = "1000", maxMessagesPerPoll = "1"))
    public Supplier<Loan> supplyLoan() {

        Supplier<Loan> loanSupplier = () -> {
            Loan loan = new Loan(UUID.randomUUID().toString(),
                names.get(new Random().nextInt(names.size())),
                amounts.get(new Random().nextInt(amounts.size())));
            log.info("{} {} for ${} for {}", loan.getStatus(), loan.getUuid(), loan.getAmount(), loan.getName());
            return loan;
        };

        return loanSupplier;
    }
}
