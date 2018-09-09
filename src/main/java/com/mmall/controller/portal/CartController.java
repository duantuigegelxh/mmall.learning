package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 *
 */
@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;


    //查询商品
    @RequestMapping("list.do")
    @RequestBody
    public ServerResponse<CartVo> list(HttpSession session,Integer count,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.list(user.getId());
    }



    //添加商品到购物车里面
    @RequestMapping("add.do")
    @RequestBody
    public ServerResponse<CartVo> add(HttpSession session,Integer count,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.add(user.getId(),productId,count);
    }

    // 更新购物车
    @RequestMapping("update.do")
    @RequestBody
    public ServerResponse<CartVo> update(HttpSession session,Integer count,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.add(user.getId(),productId,count);//这里表示会进行重新校验，这里首先会在db中进行校验，也保证了我们数据的正确性
    }


    //删除产品
    @RequestMapping("delete_product.do")
    @RequestBody
    public ServerResponse<CartVo> deleteProduct(HttpSession session,String productIds){//这个id是和前端进行了约定，传过来的是一个用“，”进行分隔的字符串，可以有多个id
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.deleteProduct(user.getId(),productIds);//这里表示会进行重新校验，这里首先会在db中进行校验，也保证了我们数据的正确性
    }



    // 全选
    @RequestMapping("select_all.do")
    @RequestBody
    public ServerResponse<CartVo> selectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.selectOrUnSelect(user.getId(),null,Const.Cart.CHECKED);//这里表示会进行重新校验，这里首先会在db中进行校验，也保证了我们数据的正确性
    }

    //全反选
    @RequestMapping("un_select_all.do")
    @RequestBody
    public ServerResponse<CartVo> unSelectAll(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.selectOrUnSelect(user.getId()null,,Const.Cart.UN_CHECKED);//这里表示会进行重新校验，这里首先会在db中进行校验，也保证了我们数据的正确性
    }


    //单独反选
    @RequestMapping("un_select.do")
    @RequestBody
    public ServerResponse<CartVo> unSelect(HttpSession session,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.UN_CHECKED);//这里表示会进行重新校验，这里首先会在db中进行校验，也保证了我们数据的正确性
    }

    //单独选
    @RequestMapping("select.do")
    @RequestBody
    public ServerResponse<CartVo> Select(HttpSession session,Integer productId){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDEsc());
        }
        return iCartService.selectOrUnSelect(user.getId(),productId,Const.Cart.CHECKED);//这里表示会进行重新校验，这里首先会在db中进行校验，也保证了我们数据的正确性
    }

    // 查询当前用户的购物车的产品数量，如果有一个产品有10个，那么数量为10
    @RequestMapping("get_cart_product_count.do")
    @RequestBody
    public ServerResponse<CartVo> get_cart_product_count(HttpSession session){
        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){//当用户不存在时，提示需要登录
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }
}











