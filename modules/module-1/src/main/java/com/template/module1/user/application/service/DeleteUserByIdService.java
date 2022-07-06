package com.template.module1.user.application.service;

import com.template.module1.user.application.port.persistance.WriteUserPort;
import com.template.module1.user.application.usecase.DeleteUserByIdUserCase;
import com.template.module1.user.domain.UserId;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserByIdService implements DeleteUserByIdUserCase {

    private final WriteUserPort writeUserPort;

    DeleteUserByIdService(WriteUserPort writeUserPort) {
        this.writeUserPort = writeUserPort;
    }

    @Override
    public void deleteById(UserId userId) {
        //check if exists with validate?
        writeUserPort.deleteById(userId);
    }
}
