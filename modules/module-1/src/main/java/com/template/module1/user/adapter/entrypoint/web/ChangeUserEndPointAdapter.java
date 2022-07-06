package com.template.module1.user.adapter.entrypoint.web;

import com.template.module1.user.application.port.entrypoint.ChangeUserEndPointPort;
import com.template.module1.user.application.usecase.AddUserUseCase;
import com.template.module1.user.application.usecase.ChangeExistingUserUseCase;
import com.template.module1.user.application.usecase.DeleteUserByIdUserCase;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class ChangeUserEndPointAdapter implements ChangeUserEndPointPort {

    private final AddUserUseCase addUserUseCase;
    private final ChangeExistingUserUseCase changeExistingUserUseCase;
    private final DeleteUserByIdUserCase deleteUserByIdUserCase;

    ChangeUserEndPointAdapter(AddUserUseCase addUserUseCase,
        ChangeExistingUserUseCase changeExistingUserUseCase,
        DeleteUserByIdUserCase deleteUserByIdUserCase) {
        this.addUserUseCase = addUserUseCase;
        this.changeExistingUserUseCase = changeExistingUserUseCase;
        this.deleteUserByIdUserCase = deleteUserByIdUserCase;
    }

    @Override
    public User saveUser(User user) {
        return addUserUseCase.saveUser(user);
    }

    @Override
    public User updateUser(User user) {
        return changeExistingUserUseCase.updateUser(user);
    }

    @Override
    public void deleteUser(UserId userId) {
        deleteUserByIdUserCase.deleteById(userId);
    }

}
