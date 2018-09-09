package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    //根据用户ID还有产品ID去查询这个购物车
    Car  selectCartByUserIdProductId(@Param("userId") Integer userId,@Param("productId") Integer productId);

    //通过用户Id查询购物车
    List<Cart> selectCartByUserId(Integer userId);

    //在更新操作时用到了这个方法
    int selectCartProductCheckedStatusByUserId(Integer userId);

    //删除
    int deleteByUserIdProductIds(@Param("userId") Integer userId,@Param("productId")List<String> productIdList);

    //全选或者不全选
    int checkOrUnchecedProduct(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("checkd") Integer checked);//根据传过来的参数，我们将其设置为全选或者全不选

    //查询当前用户的购物车的产品数量，如果有一个产品有10个，那么数量为10
    int selectCartProductCount(@Param(userid)Integer userid);

    //返回购物车里面的信息
    List<Cart> selectCheckedCartByUserId(Integer userId);
}