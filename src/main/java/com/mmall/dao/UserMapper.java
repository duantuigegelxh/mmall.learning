package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    // 通过查看源码得知，这个是根据不为空的更新
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    //核实用户数目
    int checkUsername(String username);

    //判断用户的emil
    int checkEmail(String email);
    //根据用户名和密码核实数据库中是否存在这个用户并且返回此用户的信息。
    User selectLogin(@Param("username") String username,@Param("password") String password);//但传入多个参数的时候，需要用@Param注解，注解里面的值便是我们写SQL的时候的名称

    //通过username查找相关问题
    String selectQuestionByUsername(String username);

    //校验问题是否正确
    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);//多个参数，需要加Param注解

    //通过用户名更改密码
    int updatePasswordByUsername(@Param("username") String username,@Param("passwordNew") String passwordNew);
    //检查密码是否正确，为了防止横向越权，因此这里我需要查询密码的同时，还需要和用户id一起
    int checkPassword(@Param("password") String password,@Param("userId") Integer userId);

    //用过用户id更改email
    int checkEmailByUserId(@Param(value="password") String email,@Param(value="userId") Integer userId);
}

















