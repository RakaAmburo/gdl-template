package com.template.module1.user.adapter.entrypoint.web;

import com.template.module1.user.application.port.entrypoint.FindUserEndPointPort;
import com.template.module1.user.application.usecase.FindAllUserUseCase;
import com.template.module1.user.application.usecase.FindUserByIdUserCase;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Collection;
import org.springframework.stereotype.Component;

@Component
public class FindUserEndPointAdapter implements FindUserEndPointPort {

    private final FindAllUserUseCase findAllUserUseCase;
    private final FindUserByIdUserCase findUserByIdUserCase;

    FindUserEndPointAdapter(FindAllUserUseCase findAllUserUseCase,
        FindUserByIdUserCase findUserByIdUserCase){
        this.findAllUserUseCase = findAllUserUseCase;
        this.findUserByIdUserCase = findUserByIdUserCase;
    }

    @Override
    public Collection<User> fetchAllUsers() {
        return findAllUserUseCase.fetchAllUsers();
    }

    @Override
    public User fetchUserById(Integer userId) {
        return findUserByIdUserCase.findById(UserId.of(userId));
    }
}
