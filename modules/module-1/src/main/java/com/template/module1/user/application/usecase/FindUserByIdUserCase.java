package com.template.module1.user.application.usecase;

import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;

public interface FindUserByIdUserCase {
    User findById(UserId userId);
}
