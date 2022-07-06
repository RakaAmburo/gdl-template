package com.template.module1.user.adapter.entrypoint.web.model;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    @NotBlank
    private final String firstName;

    @NotBlank
    private final String lastName;

}
