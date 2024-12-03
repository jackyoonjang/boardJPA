package org.example.board.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.board.dto.Board;
import org.example.board.dto.LoginInfo;
import org.example.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시물 목록을 보여준다.
    // 컨트롤러의 메소드가 리턴하는 문자열은 템플릿 이름이다.
    // http://localhost:8080/ -----> "list"라는 템플릿을 사용(forward)하여 화면에 출력
    @GetMapping("/")
    public String list(@RequestParam(name = "page", defaultValue = "1") int page, HttpSession session, Model model) { // HttpSession 세션의 값 사용하기, Model 템플릿에 값을 전달하기
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        model.addAttribute("loginInfo", loginInfo);

        // 관리자일경우 관리자 표시
        if(loginInfo != null) {
            List<String> roles = loginInfo.getRoles();
            if(roles.contains("ROLE_ADMIN")) {
                model.addAttribute("role", "관리자");
            }
        }

        int totalCount = boardService.getTotalCount(); // 총 row 수
        List<Board> list = boardService.getBoards(page); // page가 1,2,3,4....
        int pageCount = totalCount / 10; // 총 페이지 수
        if (totalCount % 10 > 0) { // 나머지가 있을 경우 1page 추가(23 -> 3page, 54 -> 6page)
            pageCount++;
        }
        int currentPage = page; // 현재 페이지

        // 시작 페이지의 초기값은 1
        // 현재 10페이지를 넘어가면 시작페이지는 11
        int startPage = page > 10 ? page/10 *10 : 1;

        // 마지막 페이지는 시작 페이지 + 9
        int endPage = startPage == totalCount/10 - totalCount/10%10 ? startPage + totalCount/10%10 + 1 : startPage + 9;
        // 하지만 시작 페이지가 마지막장의 시작 페이지일 경우
        // 종료페이지는 총 페이지 나누기 10한 나머지가 되어야 한다.



        model.addAttribute("list", list);
        model.addAttribute("pageCount", pageCount);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        System.out.println("pageCount: " + pageCount + " totalCount: " + totalCount + " currentPage: " + currentPage + " startPage: " + startPage + " endPage: " + endPage);
        return "list";
    }

    @GetMapping("/board")
    public String board(
            @RequestParam("boardId") int boardId,
            @RequestParam("flag") boolean flag,
            Model model
    ) {
        System.out.println("boardId: " + boardId);

        System.out.println("flag: " + flag);

        Board board = boardService.getBoard(boardId,flag);
        model.addAttribute("board", board);
        return "board";
    }

    @GetMapping("/writeForm")
    public String writeForm(HttpSession session, Model model) {
        // 로그인한 사용자만 글을 써야한다.
        // 세션에서 로그인한 정보를 읽어들인다.
        // 로그인을 하지 않았다면 리스트보기로 자동 이동 시킨다.
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인 폼으로 돌아간다.
            return "redirect:/loginForm";
        }
        model.addAttribute("loginInfo", loginInfo);
        return "writeForm";
    }

    @PostMapping("/write")
    public String write(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ){
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) { // 세션에 로그인 정보가 없으면 로그인 폼으로 돌아간다.
            return "redirect:/loginForm";
        }
        // 로그인한 사용자만 글을 써야한다.
        // 세션에서 로그인한 정보를 읽어들인다. 로그인 하지 않았다면 리스트보기로 자동이동 시킨다.
        System.out.println("title: " + title);
        System.out.println("content: " + content);
        // 로그인 한 회원 정보 + 제목, 내용을 저장한다.
        boardService.addBoard(loginInfo.getUserId(),title,content);

        return "redirect:/"; // 리스트 보기로 리다이렉트한다.
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("boardId") int boardId, HttpSession session) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginForm";
        }
        // 이 글의 주인과 로그인한 사용하의 id가 같으냐?
        List<String> roles = loginInfo.getRoles();
        if(roles.contains("ROLE_ADMIN")) {
            boardService.deleteBoard(boardId);
        }else{
            boardService.deleteBoard(loginInfo.getUserId(), boardId);
        }

        return "redirect:/";
    }

    @GetMapping("/updateform")
    public String updateForm(@RequestParam("boardId") int boardId,
                             HttpSession session,
                             Model model) {
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginForm";
        }
        // boardId에 해당하는 정보를 읽어와서 updateform 템플릿에게 전달한다.
        Board board = boardService.getBoard(boardId, false);
        model.addAttribute("board", board);
        model.addAttribute("loginInfo", loginInfo);
        return "updateform";
    }

    @PostMapping("/update")
    public String update(
            @RequestParam("boardId") int boardId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            HttpSession session
    ){
        LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
        if (loginInfo == null) {
            return "redirect:/loginForm";
        }
        Board board = boardService.getBoard(boardId, false);
        if(board.getUserId() != loginInfo.getUserId()) {
            return "redirect:/board?boardId=" + boardId; // 글보기로 이동한다.
        }
        // 숨겨서 보내준 boardId에 해당하는 글의 제목과 내용을 수정한다.
        boardService.updateBoard(boardId, title, content);
        // 글쓴이만 수정 가능.
        return "redirect:/board?boardId=" + boardId + "&flag=" + false; // 수정된 글 보기로 리다이렉트.
    }


}
