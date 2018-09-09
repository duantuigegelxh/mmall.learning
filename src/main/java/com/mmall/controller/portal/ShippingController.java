package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
/**
 *收获地址模块
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;


    // 增加地址的接口
    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session,Shipping shipping){
        User user=(user) session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());

        //使用springMVC的对象绑定可以减少方法参数，就不用在方法参数上写过多的参数了
        return iShippingService.add(user.getId(),shipping);
    }


    // 删除地址的接口
    @RequestMapping("del.do")
    @ResponseBody
    public ServerResponse del(HttpSession session, Integer shippingId){
        /**
         * 这里面会有横向越权的漏洞，因为我们知识一个普通用户，没有管理员，但是我登录完后，传一个shippingId，传的不是自己的
         * shippingId,那么就会把传过来的 shippingId给删除，改进的方法是改写mapper.xml中的sql语句,再判断一下用户的userId
         */
        User user=(user) session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());

        //使用springMVC的对象绑定可以减少方法参数，就不用在方法参数上写过多的参数了
        return iShippingService.del(user.getId(),shippingId);
    }

    // 更新地址的接口
    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session,Shipping shipping){
        User user=(user) session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());


        return iShippingService.update(user.getId(),shipping);
    }


    // 查询地址的接口
    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId){
        /**
         * 这里面会有横向越权的漏洞，因为我们知识一个普通用户，没有管理员，但是我登录完后，传一个shippingId，传的不是自己的
         * shippingId,那么就会把传过来的 shippingId给删除，改进的方法是改写mapper.xml中的sql语句,再判断一下用户的userId
         */
        User user=(user) session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());

        //使用springMVC的对象绑定可以减少方法参数，就不用在方法参数上写过多的参数了
        return iShippingService.select(user.getId(),shippingId)
    }

    //一个分页的接口
    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> list(@RequestParam(value="pageNum",defaultValue = "1")int pageNum,
                                         @RequestParam(value="pageSize" defaultValue="10")int pageSize
                                          HttpSession session){
        User user=(user) session.getAttribute(Const.CURRENT_USER);
        if(user==null)
            return iShippingService.list(user.getId(),pageNum,pageSize);
    }
}








