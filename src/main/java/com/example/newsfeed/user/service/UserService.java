package com.example.newsfeed.user.service;

import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.user.dto.LoginResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public LoginResponseDto login(String email, String password) {
        User findedUser = userRepository.findByEmailOrElseThrow(email);

        if(encoder.matches(password, findedUser.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 틀렸습니다.");
        }

        return new LoginResponseDto(findedUser.getEmail());
    }
}
