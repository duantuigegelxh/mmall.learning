package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService{

    @Autowired
    private ShippingMapper shippingMapper;

    //添加 收获地址方法
    public ServerResponse add(Integer userId,Shipping shipping){
        //因为没有让前台传userId，所以我们在这里需要进行赋值
        shipping.setUserId(userId);
        /*
         <insert id="insert" parameterType="com.mmall.pojo.Shipping" useGeneratedKeys="true" keyProperty="id">
         <!--使用MyBatis往MySQL数据库中插入一条记录后，需要返回该条记录的自增主键值。-->
         这样后，主键id就会自动填充到shipping上
         */
        int rowCount=shippingMapper.insert(shipping);
        if(rowCount>0){
            Map result=Maps.newHashMap();
            result.put("shippingID",shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    //删除地址的方法
    public ServerResponse.del(Integer userId,Integer shippingId){

        int resultCount=shippingMapper.deleteByShippingIdUserId(userId,shippingId);
        if(resultCount>0){
            return ServerResponse.createBySuccess("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    // 更新地址的方法
    public ServerResponse.update(Integer userId,Shipping shipping){

        shipping.setUserId(userId);//这里重复赋值是为了防止横向越权，以防止传过来的shipping中的userId不是我们想要的
        int resultCount=shippingMapper.updateByShipping(shipping);//我们在这里重新写一个update方法，因为自带的userId方法不会判断userId        if(resultCount>0){
            return ServerResponse.createBySuccess("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }


    //查询地址的接口
    public ServerResponse<Shipping> select(Integer userId,Integer shippingId) {
        Shipping shipping=shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping==null)
            return ServerResponse.createByErrorMessage("无法查询到该地址");
        return ServerResponse.createBySuccess("更新地址成功",shipping)
    }

    //一个分页的逻辑
    public ServerResponse<PageInfo> list(Integer userId,int pageNum,int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList=shippingMapper.selectByUserId(userId);
        PageInfo pageInfo=new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageInfo);
    }
}











