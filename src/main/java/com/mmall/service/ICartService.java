package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVo;
/**
 *
 */
public interface ICartService {
    //添加商品到购物车
    ServerResponse<CartVo> add(Integer userId,Integer productId,Integer count);

    //更新商品
    ServerResponse<CartVo> update(Integer userId,Integer productId,Integer count);

    //删除商品
    ServerResponse<CartVo> deleteProduct(Integer userId,String productIds);

    //查询商品
    ServerResponse<CartVo> list(Integer userId);

    //全选或者不全选
    ServerResponse<CartVo> selectOrUnSelect(Integer userId,Integer productId,Integer checked);

    // 查询当前用户的购物车的产品数量，如果有一个产品有10个，那么数量为10
    ServerResponse<Integer> getCartProductCount(Integer userId);
}
