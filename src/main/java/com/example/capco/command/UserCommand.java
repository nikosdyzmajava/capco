package com.example.capco.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class UserCommand {

    private String name;
    private String login;
    private String password;
    private String role;
    private boolean enabled;
}
