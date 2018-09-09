package com.mmall.dao;

import com.mmall.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);


    //返回一个order对象
    Order selectByUserIDAndOrderNo(@Param("userId")Integer userId,@Param("orderNo")Long orderNo);



    //通过支付号查询支付信息
    Order selectByOrderNo(Long orderNo);

    //获取订单集合
    List<Order> selectByUserId(Integer userId);

    //获取全部的订单
    List<Order> selectAllOrder();
}