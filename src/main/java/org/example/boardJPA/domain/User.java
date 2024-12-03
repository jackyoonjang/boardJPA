package org.example.boardJPA.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity // 데이터베이스 테이블과 맵핑하는 객체
@Table(name = "user")//database 테이블 이름 user3 와 User라는 객체가 맵핑.
@NoArgsConstructor // 기본 생성자
@Setter
@Getter
//@ToString
public class User {
    @Id // 이 필드가 Table의 PK
    @Column(name = "user_id") // 테이블의 컬럼을 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // userId는 자동으로 생성되도록 한다.
    private Integer userId;
    @Column(length = 255)
    private String email;
    @Column(length = 50)
    private String name;
    @Column(length = 500)
    private String password;

    @CreationTimestamp // 현재시간이 자동으로 생성
    private LocalDateTime regdate;

    // User ---> Role 단방향.
    @ManyToMany // 여러 사용자는 여러 권한을 가진다. N대N
    @JoinTable(name = "user_role", // 관계테이블 설정
            joinColumns = @JoinColumn(name = "user_id"), // 관계테이블과 참조하는 키 작성(나의 키)
            inverseJoinColumns = @JoinColumn(name = "role_id")) // 관계테이블이 상대랑 참조하는 키 작성(상대의 키)
    Set<Role> roles = new HashSet<>();


    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", regdate=" + regdate +
                '}';
    }
}
