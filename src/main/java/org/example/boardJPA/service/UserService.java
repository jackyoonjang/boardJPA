package org.example.boardJPA.service;

import lombok.RequiredArgsConstructor;
import org.example.boardJPA.domain.Role;
import org.example.boardJPA.domain.User;
import org.example.boardJPA.repository.RoleRepository;
import org.example.boardJPA.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// 트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
// 스프링이 관리하는 Bean
@Service
@RequiredArgsConstructor // lombok이 final 필드를 초기화 하는 생성자를 자동으로 생성한다.
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public User addUser(String name, String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        Role role = roleRepository.findByName("ROLE_USER").get();
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));
        user = userRepository.save(user); // 유저 생성 및 유저의 아이디 값 저장
        return user;
    };

    @Transactional
    public User getUser(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(int userId) {
        Set<Role> roles = userRepository.findById(userId).orElseThrow().getRoles();
        List<String> list = new ArrayList<>();
        for (Role role : roles) {
            list.add(role.getName());
        }
        return list;
    }
}
