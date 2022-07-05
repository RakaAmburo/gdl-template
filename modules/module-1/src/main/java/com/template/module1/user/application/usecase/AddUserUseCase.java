package com.template.module1.user.application.usecase;

import com.template.module1.user.domain.User;

public interface AddUserUseCase {
    User saveUser(User user);
}
