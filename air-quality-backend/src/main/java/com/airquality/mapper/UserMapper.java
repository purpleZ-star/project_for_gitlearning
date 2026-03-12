package com.airquality.mapper;

import com.airquality.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    User findByUsername(@Param("username") String username);

    List<User> findAll();

    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);

    User findById(@Param("id") Long id);
}
