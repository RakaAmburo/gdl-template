package com.template.module1.user.adapter.entrypoint.web;

import com.template.module1.user.adapter.entrypoint.web.model.UserDto;
import com.template.module1.user.application.port.entrypoint.ChangeUserEntryPointPort;
import com.template.module1.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ChangeUserEntryPointPort changeUserEntryPointPort;
    private final UserDtoMapper userDtoMapper;

    UserController(ChangeUserEntryPointAdapter changeUserEntryPointPort, UserDtoMapper userDtoMapper) {
        this.changeUserEntryPointPort = changeUserEntryPointPort;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody @Valid UserDto userDto) {
        User user = userDtoMapper.toDomainFromSaveBody(userDto);
        User savedUser = changeUserEntryPointPort.saveUser(user);
        return userDtoMapper.toDto(user);
    }
}
