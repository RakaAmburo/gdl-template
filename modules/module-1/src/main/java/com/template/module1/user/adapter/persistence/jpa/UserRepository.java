package com.template.module1.user.adapter.persistence.jpa;

import com.template.module1.user.adapter.persistence.jpa.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserData, Integer> {
}
