package org.example.Mapper;

import org.example.JavaBeans.User;
import org.example.OrmAnnotations.MyMapper;
import org.example.OrmAnnotations.MySelect;

import java.util.List;

/**
 * @author ziyuan
 * @since 2024.04
 */
@MyMapper
public interface UserMpper {
    @MySelect("select * from user where UserId = #{userId} and UserName = #{userName} limit 1")
    User getOneUser(Integer userId, String userName);
    @MySelect("select * from user ")
    List<User> getAllUser();
}
