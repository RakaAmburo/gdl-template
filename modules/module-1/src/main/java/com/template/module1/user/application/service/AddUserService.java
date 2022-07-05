package com.template.module1.user.application.service;

import com.template.module1.user.application.port.persistance.WriteUserPort;
import com.template.module1.user.application.usecase.AddUserUseCase;
import com.template.module1.user.domain.User;
import org.springframework.stereotype.Service;

@Service
public class AddUserService implements AddUserUseCase {

    private final WriteUserPort writeUserPort;

    AddUserService(WriteUserPort writeUserPort) {
        this.writeUserPort = writeUserPort;
    }

    @Override
    public User saveUser(User user) {

        //check if user exists (and other checks) before saves or...
        return writeUserPort.save(user);
    }
}
