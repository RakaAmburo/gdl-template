package com.template.module1.user.application.port.entrypoint;

import com.template.module1.user.domain.User;

public interface ChangeUserEntryPointPort {

    User saveUser(User user);
}
