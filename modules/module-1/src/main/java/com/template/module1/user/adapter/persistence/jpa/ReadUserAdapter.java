package com.template.module1.user.adapter.persistence.jpa;

import com.template.module1.user.application.port.persistance.ReadUserPort;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ReadUserAdapter implements ReadUserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    ReadUserAdapter(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> fetchById(UserId userId) {
        return userRepository.findById(userId.getValue()).map(userMapper::toDomain);
    }

    @Override
    public Collection<User> fetchAll() {
        return userRepository.findAll().stream().map(userMapper::toDomain).collect(Collectors.toUnmodifiableList());
    }
}
