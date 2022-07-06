package com.template.module1.user.adapter.persistence.jpa;

import com.template.module1.user.adapter.persistence.jpa.model.UserData;
import com.template.module1.user.application.port.persistance.WriteUserPort;
import com.template.module1.user.domain.User;
import com.template.module1.user.domain.UserId;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class WriteUserAdapter implements WriteUserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    WriteUserAdapter(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserData userData = userMapper.toJpaEntity(user);
        UserData savedUser = userRepository.save(userData);
        return userMapper.toDomain(savedUser);
    }

    @Override
    public Optional<User> update(User user) {

        return userRepository.findById(user.getUserId().intValue()).map(saved -> userMapper.toJpaEntity(user, saved))
            .map(userRepository::save).map(userMapper::toDomain);

    }

    @Override
    public void deleteById(UserId userId) {
            userRepository.deleteById(userId.getValue());
    }
}
