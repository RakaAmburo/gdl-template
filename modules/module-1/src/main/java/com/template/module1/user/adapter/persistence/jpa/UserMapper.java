package com.template.module1.user.adapter.persistence.jpa;

import com.template.module1.user.adapter.persistence.jpa.model.UserData;
import com.template.module1.user.domain.FullName;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    UserData toJpaEntity(User user) {

        return UserData.builder().id(user.getUserId().getValue()).firstName(user.getFullName().getFirstName())
            .lastName(user.getFullName().getLastName()).build();
    }

    User toDomain(UserData userData) {
        return User.builder().userId(UserId.of(userData.getId()))
            .fullName(FullName.of(userData.getFirstName(), null, userData.getLastName())).build();
    }
}
