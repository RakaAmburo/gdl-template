package com.template.module1.user.application.port.entrypoint;

import com.template.module1.user.domain.User;
import java.util.Collection;

public interface FindUserEndPointPort {

    Collection<User> fetchAllUsers();

    User fetchUserById(Integer userId);
}
