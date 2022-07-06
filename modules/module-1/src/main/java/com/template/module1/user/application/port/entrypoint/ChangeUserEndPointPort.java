package com.template.module1.user.application.port.entrypoint;

import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;

public interface ChangeUserEndPointPort {

    User saveUser(User user);

    User updateUser(User user);

    void deleteUser(UserId userId);
}
