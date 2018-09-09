package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
/**
 */
public class IShippingService {
    //添加收货地址
    ServerResponse add(Integer userId,Shipping shipping);

    //删除收货地址
    ServerResponse.del(Integer userId,Integer shippingId);

    //更新地址
    ServerResponse.update(Integer userId,Shipping shipping);

    //查询 地址的接口
    ServerResponse<Shipping> select(Integer userId,Integer shippingId);

    //分页
    ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize);


}
