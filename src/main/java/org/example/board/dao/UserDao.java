package org.example.board.dao;

import org.example.board.dto.User;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLPermission;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class UserDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsertOperations insertUser;
    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("user") // 테이블 이름
                .usingGeneratedKeyColumns("user_id"); // 자동으로 증가되는 id를 설정.
    }

    // Spring JDBC를 이용한 코드.

    // insert into user(email, name, password, regdate) values (?, ?, ?, now()); # user_id auto gen
    // select LAST_INSERT_ID();
    @Transactional
    public User addUser(String name, String email, String password){
        // Service에서 이미 트랜잭션이 시작했기 때문에, 그 트랜잭션에 포함된다.(새로 트랜잭션을 생성하지 않는다.)
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRegdate(LocalDateTime.now());
        SqlParameterSource params = new BeanPropertySqlParameterSource(user);
        Number number = insertUser.executeAndReturnKey(params);// insert를 실행하고, 자동으로 실행된 id값을 가져온다.
        int userId = number.intValue();
        user.setUserId(userId);

        return user;
    }

    // insert into user_role(user_id, role_id) values(?, 1);
    @Transactional
    public void mappingUserRole(int userId){
        // Service에서 이미 트랜잭션이 시작했기 때문에, 그 트랜잭션에 포함된다.(새로 트랜잭션을 생성하지 않는다.)
        String sql = "insert into user_role(user_id, role_id) values(:userId, 1)";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        jdbcTemplate.update(sql,params);
    }

    @Transactional
    public User getUser(String email) {
        try {
            String sql = "select user_id, email, name, password, regdate from user where email = :email";
            SqlParameterSource params = new MapSqlParameterSource("email", email);
            RowMapper<User> rowMapper = BeanPropertyRowMapper.newInstance(User.class);
            User user = jdbcTemplate.queryForObject(sql, params, rowMapper);
            return user;
        }catch (Exception e){
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<String> getRoles(int userId) {
        String sql = "select r.name from user_role ur, role r where ur.role_id = r.role_id and ur.user_id = :userId";
        SqlParameterSource params = new MapSqlParameterSource("userId", userId);
        List<String> rolse= jdbcTemplate.query(sql, Map.of("userId",userId),
                (rs, rowNum) -> rs.getString(1));
        return rolse;
    }
}
