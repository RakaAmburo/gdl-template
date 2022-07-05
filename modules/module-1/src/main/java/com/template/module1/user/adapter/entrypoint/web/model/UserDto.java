package com.template.module1.user.adapter.entrypoint.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    private final String firstName;

    private final String lastName;

}
