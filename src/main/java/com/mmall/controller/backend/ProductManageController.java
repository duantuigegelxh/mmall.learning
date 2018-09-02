package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 产品管理模块
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    // 保存商品的接口
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session,Product product){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }


    //产品上下架,产品的状态,更新产品的销售状态
    @RequestMapping("set_sale.status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer status){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return iProductService.setSaleStatus();
        }else{
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }


    //获取产品详情的接口
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session,Integer productId){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务
            return iProductService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }





    //管理后台，关于产品的一个list
    @RequestMapping("List.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session,@RequestParam(value="pageNum",defaultValue ="1") int pageNum,@RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务,添加一个动态分页
           return IProductService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }

    //后台商品搜索功能开发
    @RequestMapping("search.do")
    @RequestBody
    public ServerResponse productSearch(HttpSession session,String productName,String productId,,@RequestParam(value="pageNum",defaultValue ="1") int pageNum,@RequestParam(value="pageSize",defaultValue = "10") int pageSize){
        User user =(User) session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //填充业务,添加一个动态分页
            return IProductService.searchProduct(productName,productId,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorCodeMessage("无权限操作");
    }

}



















