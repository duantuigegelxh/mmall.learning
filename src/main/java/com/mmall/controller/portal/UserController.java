package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.sun.corba.se.spi.activation.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpSession;
/**
 *普通用户Controller
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;
    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value="login.do",method=RequestMethod.POST)
    @ResponseBody
    //这是一个登录接口
    public ServerResponse<User> login(String username,String password,HttpSession session){
        ServerResponse<User> response=iUserService.login(username,password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());//将此会话标记为最近登录的用户,response.getData为User的对象
        }
        //service-->mybatis-->dao
        return response;
    }

    @RequestMapping(value="logout.do",method = RequestMethod.POST)
    // 这是一个注销接口
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    //这是一个注册接口
    @RequestMapping(value="register.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return iUserService.register(user);
    }


    //这是一个校验接口，是登录用户后的一个接口
    @RequestMapping(value="check_valid.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str,String type){
        return IUserService.checkValid(str,type);
    }

    @RequestMapping(value="get_user_info.do",method=RequestMethod.POST)
    @ResponseBody
    //获得登录用户信息的请求的接口
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user=(User) session.getAttribute(Const.CURRENT_USER);//获取当前用户
        if(user!=null){
            return ServerResponse.createBySuccess(user);
        }

        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户的信息");
    }


    //忘记密码
    @RequestMapping(value="forget_get_question.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return IUserService.selectQuestion(username);
    }


    //校验这个问题是否正确
    @RequestMapping(value="forget_check_answer.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    //将Token返回给前台之后，紧接着就是忘记密码中的重置密码，我们就需要拿到这个Token，然后和缓存里面的东西做对比
    @RequestMapping(value="forget_reset_password.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgerResetPasswoed(String username,String passwordNew,String forgetToken){
        return iUserService.forgetResertPassword(username,passwordNew,forgetToken);
    }


    //登录状态的重置密码
    @RequestMapping(value="reset_password.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session,String passwordOld,String passwordNew){//因为要判断登录状态，所以需要增加session
        User user=(User)session.getAttribute(Const.CURRENT_USER);//获取当前登录的用户
        if(user==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld,passwordNew,user);
    }


    //更新用户个人信息功能的开发
    @RequestMapping(value="update_information.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> update_information(HttpSession session,User user){
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());//因为user传过来的参数没有userid，所以需要赋值为当前的userId
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response=iUserService.updateInformation(user);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    //获取用户的详细信息
    @RequestMapping(value="update_information.do",method=RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> get_information(HttpSession session){
        //如果用户未登录，需要将用户进行强制登录
        User currentUser=(User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return ServerResponse.createByErrorMessage(ResponseCode.NEED_LOGIN.getCode(),"未登录，需要强制登录state=10");
        }
        return iUserService.getInformation(currentUser.getId())
    }













}
