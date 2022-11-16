package com.example.capco.service;

import com.example.capco.domain.User;
import com.example.capco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user) {
        return userRepository.saveAndFlush(user);
    }
}
