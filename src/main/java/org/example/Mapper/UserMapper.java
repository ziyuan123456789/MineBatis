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
public interface UserMapper {

    User getOneUser(Integer userId,String password);
    @MySelect("select * from user where UserID > #{nums}")
    List<User> getAllUser(Integer nums);

}
