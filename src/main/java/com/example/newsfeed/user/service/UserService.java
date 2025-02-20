package com.example.newsfeed.user.service;

import com.example.newsfeed.common.config.PasswordEncoder;
import com.example.newsfeed.friend.service.FriendService;
import com.example.newsfeed.user.dto.login.LoginResponseDto;
import com.example.newsfeed.user.dto.user.UpdateUserRequestDto;
import com.example.newsfeed.user.dto.user.UserResponseDto;
import com.example.newsfeed.user.entity.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final FriendService friendService;
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

    //사용자 전체조회
    public List<UserResponseDto> findAll(User loggedInUser) {
        return userRepository.findAll().stream()
                .map(user -> {
                    //본인 조회 시 모든 정보 반환
                    if (loggedInUser.getId().equals(user.getId())) {
                        return UserResponseDto.toDtoFriend(user);
                    }
                    //친구 여부에 따라 반환 정보 달라짐
                    return friendService.isFriend(loggedInUser.getId(), user.getId())
                            ? UserResponseDto.toDtoFriend(user)  //친구이면 id, name, email 반환
                            : UserResponseDto.toDtoNonFriend(user);  //친구가 아니면 id만 반환
                })
                .collect(Collectors.toList());
    }

    //사용자 선택조회
    public UserResponseDto findById(User loggedInUser, Long targetUserId) {
        User user = userRepository.findById(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 존재하지 않습니다."));

        //본인 조회 시 모든 정보 반환
        if (loggedInUser.getId().equals(targetUserId)) {
            return UserResponseDto.toDtoFriend(user);
        }

        //친구 여부에 따라 반환 정보 달라짐
        return friendService.isFriend(loggedInUser.getId(), targetUserId)
                ? UserResponseDto.toDtoFriend(user)  //친구이면 id, name, email 반환
                : UserResponseDto.toDtoNonFriend(user);  //친구가 아니면 id만 반환
    }

    public UserResponseDto updateUser(User user, UpdateUserRequestDto requestDto) {

        //이름 입력 시 업데이트
        if(requestDto.getName() != null) {

            if (requestDto.getName().isBlank()) {
                throw new IllegalArgumentException("이름은 비워둘 수 없습니다.");
            }

            // 기존 이름과 동일한 경우 예외 처리
            if (requestDto.getName().equals(user.getName())) {
                throw new IllegalArgumentException("새로운 이름이 기존 이름과 동일합니다.");
            }

            user.setName(requestDto.getName());
        }

        //이메일 입력 시 업데이트
        if(requestDto.getEmail() != null) {

            if (requestDto.getEmail().isBlank()) {
                throw new IllegalArgumentException("이메일은 비워둘 수 없습니다.");
            }

            // 기존 이메일과 동일한 경우 예외 처리
            if (requestDto.getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("새로운 이메일이 기존 이메일과 동일합니다.");
            }

            user.setEmail(requestDto.getEmail());
        }

        userRepository.save(user);

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }

    public void updatePassword(User user, String oldPassword, String newPassword) {

        //현재 비밀번호가 일치하지 않으면 예외 발생
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        //새 비밀번호가 기존 비밀번호와 동일하면 예외 발생
        if (encoder.matches(newPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다.");
        }

        //비밀번호 암호화 후 저장
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
    }

    public void delete(User user, String password) {
        if (!encoder.matches(password, user.getPassword())) {   //로그인한 유저의 비밀번호와 유저가 입력한 비밀번호의 검증
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 다릅니다.");
        }
        userRepository.delete(user);
    }

}