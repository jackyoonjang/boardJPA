package org.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
//@AllArgsConstructor 모든 필드를 생성자에서 받아야 한다.
public class LoginInfo {
    private int userId;
    private String email;
    private String name;
    private List<String> roles = new ArrayList<>(); // 로그인 해야 role값이 입력되기 때문에 초기화 해줘야 한다.

    public void addRole(String roleName) {
        this.roles.add(roleName);
    }

    public LoginInfo(int userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
    }
}
