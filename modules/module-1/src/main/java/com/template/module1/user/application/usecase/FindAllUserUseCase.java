package com.template.module1.user.application.usecase;

import com.template.module1.user.domain.User;
import java.util.Collection;

public interface FindAllUserUseCase {

    Collection<User> fetchAllUsers();
}
