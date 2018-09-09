package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.vo.OrderVo;

import java.util.Map;
/**
 */
public class IOrderService {

    //支付
    ServerResponse pay(Integer userId,String path,Long orderNo);

    //回调函数
    ServerResponse aliCallback(Map<String,String>params);

    //轮询，查询支付状态
    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);

    //创建订单
    ServerResponse createOrder(Integer userId,Integer shippingId);

    //取消订单
    ServerResponse<String> cancel(Integer userId,Long orderNo);

    //获取订购物车里面的信息
    ServerResponse getOrderCartProduct(Integer userId);

    //获取我们的订单详情
    ServerResponse<OrderVo> getOrderDetail(Integer userId,Long orderNo);

    //传一个分页功能
     ServerResponse<PageInfo> getOrderList(Integer userId,int pageNum,int pageSize);


    //backend

    //后台获取订单分页
    ServerResponse<PageInfo> manageList(int pageNum,int pageSize);

    //后台获取详情
    ServerResponse<OrderVo> manageDetail(Long orderNo);

    //后台搜索订单
    ServerResponse<PageInfo> manageSearch(Long orderNo,int pageNum,int pageSize);

    //后台发货
    ServerResponse<String> manageSendGoods(Long orderNo);
}
