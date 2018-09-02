package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
/**
 * 管理员使用的界面
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryServce;

    @RequestMapping("add_category.do")
    @ResponseBody
    /**
     *
     * @param session   校验里面的参数是否是管理员，所以放入session
     * @param categoryName
     * @param parentId 父节点,当前端没有传参数进来的时候，我们传入一个默认的值0，因为0使我们分类的根节点
     * @return
     */
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);//当前登录的用户
        if (user == null) {//当用户未登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN, "用户未登录，请登录");
        }
        ServerResponse.
        //校验一下是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //增加我们处理分类的逻辑
            return iCategoryServce.addCategory(categoryName, parentId);
        } else {//不是管理员
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }
    }


    @RequestMapping("set_category_name.do")
    @ResponseBody
    //更新Categoryname
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //更新categoryName
            return iCategoryServce.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限的操作，需要管理员权限");
        }
    }


    @RequestMapping("get_category.do")
    @ResponseBody
    //根据我们传的category_id获取当前category_id下面子节点的category的信息，并且是平级的，无递归
    public ServerResponse getChildrenParallerCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue=0) Integer categoryId){
        //当没有传入categoryId时，默认为0
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryServce.getChildrenParallerlCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");2
        }
    }


    @RequestMapping("get_deep_category.do")
    @ResponseBody
    //根据我们传的category_id获取当前category_id下面子节点的category的信息，并且是平级的，有递归
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value="categoryId",defaultValue=0) Integer categoryId){
        //当没有传入categoryId时，默认为0
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //查询当前节点的id和递归子节点的id
            return iCategoryServce.selectCategoryAndChildrenById(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");2
        }
    }
}






