package com.example.newsfeed.user.service;

import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.user.dto.login.LoginResponseDto;
import com.example.newsfeed.user.dto.user.UserResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public User findUser(String email, String password) {
        User findedUser = userRepository.findByEmailOrElseThrow(email); //email로 이미 가입된 유저인가 찾고 아니면 오류 던짐

        if(!encoder.matches(password, findedUser.getPassword())){ // 비밀번호가 저장된 것과 틀리면 오류던짐
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "아이디 혹은 비밀번호가 틀렸습니다.");
        }

        return findedUser;
    }

    public LoginResponseDto getUser(String email){  //어떤 사람인지 알아와서 응답값 responseDto로 반환하는 메서드
        User finduser = userRepository.findByEmailOrElseThrow(email);
        return new LoginResponseDto(finduser.getId(), finduser.getEmail());
    }

    public UserResponseDto save(String email, String password, String name){
        Optional<User> findUser = userRepository.findByEmail(email); //이미 가입된 유저의 이메일과 겹치나 찾아봄

        if (findUser.isPresent()) { //겹치면 오류 던짐
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다.");
        }

        String encoded = encoder.encode(password); //비밀번호 암호화

        User user = new User(email, encoded, name); //유저 entity의 필드에 검증된 값들 대입
        User savedUser = userRepository.save(user); //데이터베이스의 user 테이블에 저장
        return new UserResponseDto(savedUser.getId(), savedUser.getName(), savedUser.getEmail());
    }

    public void delete(User user, String password) {
        if (!encoder.matches(password, user.getPassword())) {   //로그인한 유저의 비밀번호와 유저가 입력한 비밀번호의 검증
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다.");
        }
        userRepository.delete(user);
    }
}
