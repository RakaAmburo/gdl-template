package com.template.module1.user.adapter.entrypoint.web;

import com.template.module1.user.adapter.entrypoint.web.model.UserDto;
import com.template.module1.user.application.port.entrypoint.ChangeUserEndPointPort;
import com.template.module1.user.application.port.entrypoint.FindUserEndPointPort;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ChangeUserEndPointPort changeUserEndPointPort;
    private final FindUserEndPointPort findUserEndPointPort;
    private final UserDtoMapper userDtoMapper;

    UserController(ChangeUserEndPointAdapter changeUserEntryPointPort, FindUserEndPointPort findUserEndPointPort,
        UserDtoMapper userDtoMapper) {
        this.changeUserEndPointPort = changeUserEntryPointPort;
        this.findUserEndPointPort = findUserEndPointPort;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@RequestBody @Valid UserDto userDto) {
        User user = userDtoMapper.toDomainFromSaveBody(userDto);
        User savedUser = changeUserEndPointPort.saveUser(user);
        return userDtoMapper.toDto(savedUser);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> fetchAllUsers() {
        return findUserEndPointPort.fetchAllUsers().stream().map(userDtoMapper::toDto)
            .collect(Collectors.toUnmodifiableList());

    }

    @PutMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@PathVariable("user_id") int userId,
        @RequestBody @Valid UserDto userDto) {
        User user = userDtoMapper.toDomainFromSaveBody(userId, userDto);
        return userDtoMapper.toDto(changeUserEndPointPort.updateUser(user));
    }

    @GetMapping("/{user_id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto fetchUserById(@PathVariable("user_id") Integer userId) {
        return userDtoMapper.toDto(findUserEndPointPort.fetchUserById(userId));
    }

    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("user_id") Integer userId) {
        changeUserEndPointPort.deleteUser(UserId.of(userId));
    }

}
