package org.example.boardJPA.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 데이터베이스 테이블과 맵핑하는 객체
@Table(name = "role")//database 테이블 이름 user3 와 User라는 객체가 맵핑.
@NoArgsConstructor // 기본 생성자
@Setter
@Getter
//@ToString // 엔티티 관계를 표현할 때는 되도록 안사용하는게 좋다. 재귀적인 문제가 생긴다.
public class Role {

    @Id // 이 필드가 Table의 PK
    @Column(name = "role_id") // 테이블의 컬럼을 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // userId는 자동으로 생성되도록 한다.
    private Integer roleId;

    @Column(length = 20)
    private String name;

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }
}
