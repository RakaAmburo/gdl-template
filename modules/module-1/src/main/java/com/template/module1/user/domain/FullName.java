package com.template.module1.user.domain;

import lombok.Getter;
import lombok.Value;

@Getter
@Value(staticConstructor = "of")
public class FullName {
    String firstName;
    String middleName;
    String lastName;
}
