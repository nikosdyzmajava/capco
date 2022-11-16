package com.example.capco.controller;

import com.example.capco.command.UserCommand;
import com.example.capco.domain.User;
import com.example.capco.dto.UserDto;
import com.example.capco.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<UserDto> add(@RequestBody UserCommand userCommand) {
        return new ResponseEntity<>(modelMapper.map(userService.addUser(modelMapper.map(userCommand, User.class)), UserDto.class),
                HttpStatus.CREATED);
    }
}
