package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 *
 */
public interface IUserService {
   //登录
   public ServerResponse<User> login(String username, String password);
   //注册
   ServerResponse<String> register(User user);
   //校验接口
   ServerResponse<String> checkValid(String str,String type);
   //忘记密码时根据用户名查找密保问题
   ServerResponse selectQuestion(String username);
   //校验问题是否正确
   ServerResponse<String> checkAnswer(String username,String question,String answer);
   //忘记密码后重置密码
   ServerResponse<String> forgetResertPassword(String username,String passwordNew,String forgetToken);

   //重置密码
   ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user);

   //更新个人接口
   ServerResponse<String> updateInformation(User user);

   //查询个人信息
   ServerResponse<User> getInformation(Integer userId);

   // 校验是否是管理员
   ServerResponse checkAdminRole(User user);
}