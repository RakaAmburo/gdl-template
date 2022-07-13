package com.template.producer.loansource;

import com.template.loan.model.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
public class ResultChecker {

    public static final Logger log = LoggerFactory.getLogger(ResultChecker.class);
    private final ResultProcessor resultProcessor;

    @Autowired
    public ResultChecker(ResultProcessor resultProcessor){this.resultProcessor = resultProcessor;}

    @StreamListener(ResultProcessor.APPROVED_IN)
    public void checkResultsApproved(Loan loan){
       log.info(loan.getName() + " Aprobado :)");
    }

    @StreamListener(ResultProcessor.DECLINED_IN)
    public void checkResultsDecliended(Loan loan){
        log.info(loan.getName() + " Denegado!!!!");
    }
}
