package org.example.boardJPA.repository;

import org.example.boardJPA.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

// Spring Data JPA Repository 를 완성. = DAO와 같다.
// 보통 인터페이스를 선언하면, 인터페이스를 구현하는 클래스 작성
// But, Spring Data JPA는 현재 인터페이스를 구현하는 Bean을 자동으로 생성한다.
public interface UserRepository extends JpaRepository<User, Integer> {

    // 조건이 있는 메소드를 직접 만들기
    Optional<User> findByName(String name); // query Method

    // where name = ? and email = ?
    Optional<User> findByNameAndEmail(String name, String email);

    // where name like ? or email = ?
    List<User> findByNameOrEmail(String name, String email);

    // where user_id between ? and ?
    List<User> findByUserIdBetween(int startUserId, int endUserId);

    // where user_id < ?
    List<User> findByUserIdLessThan(int userId);

    // where user_id <= ?
    List<User> findByUserIdLessThanEqual(int userId);

    // where user_id > ?
    List<User> findByUserIdGreaterThan(int userId);

    // where user_id >= ?
    List<User> findByUserIdGreaterThanEqual(int userId);

    // where regdate > ?
    List<User> findByregdateAfter(LocalDateTime regdate);

    // where regdate < ?
    List<User> findByregdateBefore(LocalDateTime regdate);

    // where name is null
    List<User> findByNameIsNull();

    // where name is not null
    List<User> findByNameIsNotNull();

    // where name like 와일드카드 사용(%,_);
    List<User> findByNameLike(String name);

    // where name like '입력한값%';
    List<User> findByNameStartingWith(String name);

    // where name like '%입력한값';
    List<User> findByNameEndingWith(String name);

    // where name like '%입력한값%';
    List<User> findByNameIsContaining(String name);

    // order by name asc;
    List<User> findByOrderByNameAsc();

    // order by name desc;
    List<User> findByOrderByNameDesc();

    // where regdate > ? order by name desc;
    List<User> findByRegdateAfterOrderByNameDesc(LocalDateTime day);

    // where name <> ?
    List<User> findByNameNot(String name); // **null 값은 안나온다.

    // where user_id in (...) -> ex) userRepository.findByUserIdIn(List.of(2,3));
    List<User> findByUserIdIn(Collection<Integer> userIds);

    // where user_id not in (...) -> ex) userRepository.findByUserIdIn(List.of(2,3));
    List<User> findByUserIdNotIn(Collection<Integer> userIds);

    // where flag = false
//    List<User> findByFlagTrue();
//    List<User> findByFlag(boolean flag);

//-----------------------------------------------------------------------------------------------
    // count(건수)

    // select * from user3;
    Long countBy();

    // select count(*) from user3 where name like ? (와일드카드 사용가능)
    Long countByNameLike(String name);

//-----------------------------------------------------------------------------------------------
    // exists(boolean)(존재)

    // where exists (select email from user3 where email = ?)
    boolean existsByEmail(String name);

//-----------------------------------------------------------------------------------------------
    // delete(삭제)

    // select * from user3 where name = ?
    // select 한 건수만큼 delete from user3 where user_id = ?
    // delete from 테이블명 where name like?
    int deleteByName(String name);

//-----------------------------------------------------------------------------------------------
    // distinct(중복제거)

    // select distinct * from user3 where name = ? // 모든 컬럼이 중복이 아닐때 중복이 제거.(현재는 id값이 달라서 이름이 같아도 2줄이 나온다.)
    List<User> findDistinctByName(String name);

//-----------------------------------------------------------------------------------------------
    // limit(제한, 원하는 개수)

    // select * from user3 limit 2; 처음 2건만 조회
    List<User> findFirst2By();
    List<User> findTop2By();

//-----------------------------------------------------------------------------------------------
    // 페이징 처리

    // ex) Page<User> users = userRepository.findBy(PageRequest.of(몇페이지,몇개씩, Sort.by(Sort.Direction.DESC,"regdate")));
    // for (User user : users.getContent()) {sout(user);}
    Page<User> findBy(Pageable pageable);

    // 이름 검색
    //Page<User> users = userRepository.findByName("슈퍼맨",PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"regdate")));
    Page<User> findByName(String name, Pageable pageable);

    Optional<User> findByEmail(String email);

    // 게시물 내용 검색
    // Page<Board> findByContentContaining(String title, Pageable pageable);
    // 게시물 제목 혹은 내용 검색
    // Page<Board> findByTitleContainingOrContentContaining(String title, Pageable pageable);


}
