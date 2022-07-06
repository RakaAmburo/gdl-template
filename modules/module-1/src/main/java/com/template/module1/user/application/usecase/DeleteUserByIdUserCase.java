package com.template.module1.user.application.usecase;

import com.template.module1.user.domain.UserId;

public interface DeleteUserByIdUserCase {
    void deleteById(UserId userId);
}
