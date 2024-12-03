package org.example.boardJPA.service;

import lombok.RequiredArgsConstructor;
import org.example.boardJPA.domain.Board;
import org.example.boardJPA.domain.User;
import org.example.boardJPA.repository.BoardRepository;
import org.example.boardJPA.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addBoard(int userId, String title, String content) {
        User user = userRepository.findById(userId).orElseThrow();
        Board board = new Board();
        board.setUser(user);
        board.setTitle(title);
        board.setContent(content);
        board.setRegdate(LocalDateTime.now());
        boardRepository.save(board);
    }

    @Transactional(readOnly = true) // select 할때
    public Long getTotalCount() {
        return boardRepository.getBoardsCount();
    }

    @Transactional(readOnly = true) // select 할때
    public List<Board> getBoards(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return boardRepository.findByOrderByRegdateDesc(pageable).getContent();
    }

    // updateViewCnt가 true면 글의 조회수를 증가, false면 글의 조회수를 증가하지 않도록 한다.
    @Transactional
    public Board getBoard(int boardId, boolean flag){
        Board board = boardRepository.findById(boardId).orElseThrow();
        if(flag){
            board.setViewCnt(board.getViewCnt()+1); // 해당 메소드가 종료될때 변화된 값을 감지해서 update가 실행
        }
        return board;
    }

    @Transactional
    public void deleteBoard(int userId, int boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        if (board.getUser().getUserId() == userId) {
            boardRepository.delete(board);
        }
    }

    @Transactional
    public void updateBoard(int boardId, String title, String content) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        board.setTitle(title);
        board.setContent(content);
        // 메소드가 종료되면서 트랜잭션이 종료되고, 스냅샷과 변화를 감지해서 update 진행
    }

    @Transactional
    public void deleteBoard(int boardId) {
        // 관리자 권한이면 조건없이 삭제
        boardRepository.deleteById(boardId);
    }
}
