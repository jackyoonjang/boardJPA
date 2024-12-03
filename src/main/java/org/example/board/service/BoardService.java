package org.example.board.service;

import lombok.RequiredArgsConstructor;
import org.example.board.dao.BoardDao;
import org.example.board.dto.Board;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardDao boardDao;

    @Transactional
    public void addBoard(int userId, String title, String content) {
        boardDao.addBoard(userId,title,content);
    }

    @Transactional(readOnly = true) // select 할때
    public int getTotalCount() {
        return boardDao.getTotalCount();
    }

    @Transactional(readOnly = true) // select 할때
    public List<Board> getBoards(int page) {
        return boardDao.getBoards(page);
    }

    // updateViewCnt가 true면 글의 조회수를 증가, false면 글의 조회수를 증가하지 않도록 한다.
    @Transactional
    public Board getBoard(int boardId, boolean flag){
        Board board = boardDao.getBoard(boardId);
        System.out.println(flag);
        if(flag){
            boardDao.updateViewCnt(boardId);
        }
        return board;
    }

    @Transactional
    public void deleteBoard(int userId, int boardId) {
        Board board = boardDao.getBoard(boardId);
        if (board.getUserId() == userId) {
            boardDao.deleteBoard(boardId);
        }
    }

    @Transactional
    public void updateBoard(int boardId, String title, String content) {
        boardDao.updateBoard(boardId,title,content);
    }

    @Transactional
    public void deleteBoard(int boardId) {
        // 관리자 권한이면 조건없이 삭제
        boardDao.deleteBoard(boardId);
    }
}
