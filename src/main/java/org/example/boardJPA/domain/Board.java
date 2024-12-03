package org.example.boardJPA.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity // 데이터베이스 테이블과 맵핑하는 객체
@Table(name = "board")//database 테이블 이름 user3 와 User라는 객체가 맵핑.
@NoArgsConstructor // 기본 생성자
@Setter
@Getter
//@ToString // 엔티티 관계를 표현할 때는 되도록 안사용하는게 좋다. 재귀적인 문제가 생긴다.
public class Board {

    @Id // 이 필드가 Table의 PK
    @Column(name = "board_id") // 테이블의 컬럼을 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // userId는 자동으로 생성되도록 한다.
    private Integer boardId;

    @Column(length = 100)
    private String title;

    @Lob // 대용량 텍스트 타입
    private String content; // text type

    private Integer viewCnt = 0;

    @CreationTimestamp // 현재시간이 자동으로 생성
    private LocalDateTime regdate;


    @ManyToOne(fetch = FetchType.LAZY) // 게시물 N --- 1 사용자
    // (fetch = FetchType.EAGER) -> 무조건 가져와라.
    // (fetch = FetchType.LAZY) -> 사용할 때 가져와라.
    @JoinColumn(name = "user_id") // 상대와 참조하고 있는 키
    private User user;

    @Override
    public String toString() {
        return "Board{" +
                "boardId=" + boardId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", viewCnt=" + viewCnt +
                ", regdate=" + regdate +
//                ", user=" + user +
                '}';
    }
}
