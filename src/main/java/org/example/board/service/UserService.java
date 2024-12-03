package org.example.board.service;

import lombok.RequiredArgsConstructor;
import org.example.board.dao.UserDao;
import org.example.board.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 트랜잭션 단위로 실행될 메소드를 선언하고 있는 클래스
// 스프링이 관리하는 Bean
@Service
@RequiredArgsConstructor // lombok이 final 필드를 초기화 하는 생성자를 자동으로 생성한다.
public class UserService {

    private final UserDao userDao;

    // 보통 서비스에서는 @Transactional 을 붙여서 하나의 트랜잭션으로 처리하게 한다.
    // Spring Boot는 트랜잭션을 처리해주는 트랜잭션 관리자를 가지고 있다.
    @Transactional
    public User addUser(String name, String email, String password){
        // 회원가입에 입력된 email로
        // userDao에서 만든 DB에서 객체를 가져오는 getUser(email) 메소드를 실행했을 때
        // 반환되는 객체가 없으면 DB에 존재하지 않는 Email이 된다.
        User user1 = userDao.getUser(email);
        if (user1 != null){
            throw new RuntimeException("이미 가입된 이메일입니다.");
        }

        // 트랜잭션 시작
        User user = userDao.addUser(name, email, password); // 유저 생성 및 유저의 아이디 값 저장
        userDao.mappingUserRole(user.getUserId()); // 권한을 부여한다.
        return user;
        // 트랜잭션 끝
    };

    @Transactional
    public User getUser(String email){
        return userDao.getUser(email);
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(int userId) {
        return userDao.getRoles(userId);
    }
}
