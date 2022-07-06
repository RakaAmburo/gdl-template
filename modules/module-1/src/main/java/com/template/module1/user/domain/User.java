package com.template.module1.user.domain;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class User {

    UserId userId;
    FullName fullName;

    public static User of(String firstName, String lastName) {
        return of(UserId.of(), FullName.of(firstName, null, lastName));
    }
}
