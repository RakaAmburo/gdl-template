package com.template.module1.user.application.port.persistance;

import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Collection;
import java.util.Optional;

public interface ReadUserPort {

    Optional<User> fetchById(UserId userId);

    Collection<User> fetchAll();
}
