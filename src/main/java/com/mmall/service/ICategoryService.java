package com.mmall.service;

/**
 *分类的接口
 */
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public class ICategoryService {
    //添加品类
    ServerResponse addCategory(String categoryName,Integer parentId);

    //更新品类，先写实现类再写接口是为了当实现类参数需要改的时候，就不用再去接口类中更改了
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);

    //查询子节点的信息
    ServerResponse<Category> getChildrenParallelCategory(Integer categoryId);

    // 递归查询子节点的信息
    ServerResponse selectCategoryAndChildreById(Integer categoryId);
}
