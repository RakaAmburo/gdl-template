package com.template.module1.user.application.port.persistance;

import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Optional;

public interface WriteUserPort {

    User save(User user);

    Optional<User> update(User user);

    void deleteById(UserId userId);
}
