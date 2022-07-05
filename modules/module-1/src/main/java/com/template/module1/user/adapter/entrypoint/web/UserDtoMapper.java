package com.template.module1.user.adapter.entrypoint.web;

import com.template.module1.user.adapter.entrypoint.web.model.UserDto;
import com.template.module1.user.domain.FullName;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    User toDomainFromSaveBody(UserDto saveUserBodyDto) {
        return User.builder()
            .userId(UserId.of(null))
            .fullName(FullName.of(saveUserBodyDto.getFirstName(), null, saveUserBodyDto.getLastName())).build();
    }

    UserDto toDto(User user) {
        return UserDto.builder()
            .firstName(user.getFullName().getFirstName())
            .lastName(user.getFullName().getLastName())
            .build();
    }

}
