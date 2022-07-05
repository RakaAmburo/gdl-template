package com.template.module1.user.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private final UserId userId;
    private final FullName fullName;
}
