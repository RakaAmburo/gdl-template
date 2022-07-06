package com.template.module1.user.application.service;

import com.template.module1.user.application.port.persistance.WriteUserPort;
import com.template.module1.user.application.usecase.ChangeExistingUserUseCase;
import com.template.module1.user.domain.User;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ChangeExistingUserService implements ChangeExistingUserUseCase {

    private final WriteUserPort writeUserPort;

    ChangeExistingUserService(WriteUserPort writeUserPort) {
        this.writeUserPort = writeUserPort;
    }

    @Override
    public User updateUser(User user) {
        return writeUserPort.update(user).orElseThrow(EntityNotFoundException::new);
    }
}
