package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import com.mysql.fabric.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;;import java.util.UUID;

/**
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    Private UserMapper userMapper;

    //登录判断
    public ServerResponse<User> login(String username, String password){
        int resultCount=userMapper.checkUsername(username);//返回用户数
         if(resultCount==0){//表示没有用户
             return ServerResponse.createByErrorMessage("用户名不存在");//输出异常信息
         }
        // 密码登录MD5，这是一个不可逆的加密方法,因为数据库中已经是加密的MD5,所以这里的密码需要处理一下,即再对密码进行加密，然后验证
        String md5Password=MD5Util.MD5EncodeUtf8(password)
        //根据用户名和密码返回数据库中的用户的信息
        User user=userMapper.selectLogin(username,md5Password);
        if(user==null){//因为到了这一步的话，用户肯定是存在的，此时返回0.则表示用户密码错误
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);//将此用户的密码设为空。
        return ServerResponse.createBySuccess("登录成功",user);
    }

    //注册用户,并判断部分信息是否已经被注册
    public ServerResponse<String> register(User user){
      /*  int resultCount=userMapper.checkUsername(User.getUsername());//根据名称往数据库里面查找，返回用户数
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("用户已存在");
        }*/
        ServerResponse validResponse=this.checkValid(user.getUsername(),Const.USERNAME);//进行代码复用，这里的代码在下面有
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse=this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }

       /* resultCount=userMapper.checkEmail(user.getEmail());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("email已存在");
        }*/

        //设置用户的角色
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密，MD5是一个非对称加密,可以在网上找一个工具类
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        //将我们这个新的user加入到我们的数据库中
        resultCount=userMapper.insert(user);
        if(resultCount==0){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


    //str是他的value值，type根据传email还是username来判断str来调用哪个。这个是一个校验接口
    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNotBlank(type)){//和isNotEmpty的区别,isNotBlank当传入值是一个" "时，表示是没有值的，而isNotEmpty表示有值
            //开始校验
            if(Const.USERNAME.equals(type)){//判断用户名
                int resultCount=userMapper.checkUsername(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("用户名已存在");
                }
            }
            if(Const.EMAIL.equals(type)){
                int resultCount=userMapper.checkEmail(str);
                if(resultCount>0){
                    return ServerResponse.createByErrorMessage("email已存在");
                }
            }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }




    //选择密码提示问题
    public ServerResponse selectQuestion(String username){

        ServerResponse validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createBySuccessMessage("用户不存在");
        }
        String question=userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    //校验密码是否正确
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCont=usermapper.checkAnswer(username,question,answer);
        if(resultCont>0){
            //说明问题及问题答案是这个用户的，并且是正确的
            String forgetToken= UUID.randomUUID().toString();//UUID.randomUUID().toString()返回的是一个不会重复的值

            //在这里边就要处理一下，把forgetToken放到本地cache中，然后设置他的有效期
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    //忘记密码中的重置密码
    public ServerResponse<String> forgetResertPassword(String username,String passwordNew,String forgetToken){
        if(StringUtils.isBlank(forgetToken)){//如果返回值为空
            return ServerResponse.createByErrorMessage("参数错误,Token需要传递");
        }

        //校验一下username
        ServerResponse validResponse=this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户名不存在
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String token=TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){//对Chache里面的Token进行一个校验
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(StringUtils.equals(forgetToken,token)){//用StringUtils的话，不用考虑空的异常，不用像a.equals("abc")担心a为null
            String md5Password =MD5Uil.MD5EncodeUtf8(passwordNew);//对密码进行MD5加密
            int rowCount=userMapper.updatePawwordByUsername(username,md5Password);//要和数据库上的密码对应，所以这里应该是MD5的密码

            if(rowCount>0){
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        }else{//当Token不一样时
            return ServerResponse.createByErrorMessage("Token错误,请重新获取重置gv密码的Token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }



    //重置密码
    public ServerResponse<String> resetPassword(String passwordOld,String passwordNew,User user){
        //防止横向越权，要校验一下这个用户的旧密码，一定要指定是这个用户，因为我们会查询一个count(1)出来，如果不指定id，那么结果就是true
        int resultCount=usermapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld),user.getId());
        if(resultCount==0){//如果等于0,说明旧密码错误
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));//将新密码赋给user
        int updateCount=userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }

    //更新个人信息的接口
    public ServerResponse<User> updateInformation(User user){
        //更新时，我们的username是不能更新的
        //同时email也要进行一个校验，校验新的email是否存在，并且存在的email如果相同的话，不能是我们当前的这个用户的
        int resullCount=userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return ServerResponse.createByErrorMessage("email已经存在，请更换email再尝试更新");
        }
        User updateUser=new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount=userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }


    //查询用户信息
    public ServerResponse<User> getInformation(Integer userId){
        User user=userMapper.selectByPrimaryKey(userId);
        if(user==null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);//StringUtils.EMPTY表示为null
        return ServerResponse.createBySuccessMessage(user);
    }


    //backend,校验一下是否是管理员
    public ServerResponse checkAdminRole(User user){
        if(user!=null && user.getRole().intValue() == Const.Role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }
}
















