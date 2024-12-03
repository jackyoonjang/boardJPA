package org.example.boardJPA.repository;

import org.example.boardJPA.domain.Board;
import org.example.boardJPA.dto.BoardIf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query(value = "select b from Board b join fetch b.user") //User의 속성을 이용해서 Join한다.
    List<Board> getBoards();

    @Query(value = "select count(b) from Board b")
    Long getBoardsCount();

    @Query(value = "select b, u from Board b inner join b.user u inner join u.roles r where r.name = :roleName")
    List<Board> getBoards(@Param("roleName") String roleName);

    //select b.board_id, b.title, b.content, b.user_id, u.name, b.regdate, b.view_cnt from board b, user u where b.user_id = u.user_id;
    @Query(value = "select b.board_id, b.title, b.content, b.user_id, u.name, b.regdate, b.view_cnt from board b, user u where b.user_id = u.user_id",
    nativeQuery = true) // SQL을 쓰겠다.
    // nativeQuery를 사용하면 결과를 담을 객체를(get필드) 새로 만들어 줘야 한다.
    List<BoardIf> getBoardsWithNativeQuery();

    // 페이징 처리로 가지고 오는데 날짜를 desc
    Page<Board> findByOrderByRegdateDesc(Pageable pageable);


}
