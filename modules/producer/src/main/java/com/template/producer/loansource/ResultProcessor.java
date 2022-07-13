package com.template.producer.loansource;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface ResultProcessor {

    String APPLICATIONS_OUT = "output";
    String APPROVED_IN = "approved";
    String DECLINED_IN = "declined";

    @Input(APPROVED_IN)
    SubscribableChannel getApproved();

    @Input(DECLINED_IN)
    SubscribableChannel getDeclined();

    @Output(APPLICATIONS_OUT)
    MessageChannel output();

}
