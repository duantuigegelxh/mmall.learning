package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import com.mysql.fabric.Server;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 */
@Service("iCategoryService")
public class CategoryServiceImpl extends ICategoryService {

    private Logger logger=LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;


    //添加类别
    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId==null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category=new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的

        int rowCount=categoryMapper.insert(category);
        if(rowCount>0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    //更新类别名称
    public ServerResponse updateCategoryName(Integer categoryId,String categoryName){
        if(categoryId == null || org.springframework.util.StringUtils.isBlank(categoryId)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category=new Category();
        Category.setId(categoryId);
        Category.setName(categoryName);

        int rowCount=categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新品类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名称失败");
    }

    //查询子节点的信息
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId){
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){//此方法不仅判断了此集合是否为空，也判断了此集合是否是没有元素
            logger.info("未找到当前分类的子分类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**递归查询子节点的信息
     * 递归查询子节点的id以及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse selectCategoryAndChildreById(Integer categoryId){
        //拿到这个后，判断子节点下面是否有子节点
        Set<Category> categorySet=Sets.newHashSet();
        findChildCategory(categorySet,categoryId);


        List<Integer> categoryIdList=Lists.newArrayList();
        if(categoryId!=null){
            for(Category categoryItem:categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }

    //一个递归函数,递归算法，算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category=categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查找子节点，递归算法一定有一个退出的程序
        List<Category> categoryList=categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for(category categoryItem:categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }

}

















