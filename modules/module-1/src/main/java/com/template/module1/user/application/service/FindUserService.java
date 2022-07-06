package com.template.module1.user.application.service;

import com.template.module1.user.application.port.persistance.ReadUserPort;
import com.template.module1.user.application.usecase.FindAllUserUseCase;
import com.template.module1.user.application.usecase.FindUserByIdUserCase;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FindUserService implements  FindAllUserUseCase, FindUserByIdUserCase {

    private final ReadUserPort readUserPort;

    FindUserService(ReadUserPort readUserPort){
        this.readUserPort = readUserPort;
    }

    @Override
    public Collection<User> fetchAllUsers() {
        return readUserPort.fetchAll();
    }

    @Override
    public User findById(UserId userId) {
        return readUserPort.fetchById(userId).orElseThrow(EntityNotFoundException::new);
    }
}
