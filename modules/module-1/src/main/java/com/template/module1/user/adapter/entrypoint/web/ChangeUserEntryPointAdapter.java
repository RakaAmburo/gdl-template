package com.template.module1.user.adapter.entrypoint.web;

import com.template.module1.user.application.port.entrypoint.ChangeUserEntryPointPort;
import com.template.module1.user.application.usecase.AddUserUseCase;
import com.template.module1.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ChangeUserEntryPointAdapter implements ChangeUserEntryPointPort {

    private final AddUserUseCase addUserUseCase;

    ChangeUserEntryPointAdapter(AddUserUseCase addUserUseCase){
        this.addUserUseCase = addUserUseCase;
    }

    @Override
    public User saveUser(User user) {
        return addUserUseCase.saveUser(user);
    }
}
