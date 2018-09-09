package com.mmall.service;

/**
 *
 */
import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

public class IProductService {
    //存储或者更新产品
    ServerResponse saveOrUpdateProduct(Product product);

    //更新产品的销售状态
    ServerResponse<String> setSaleStatus(Integer productId,Integer status);

    //获取 产品详情的接口
   ServerResponse<ProductDetailVo> manageProductDetail;

    //分页
    ServerResponse<PageInfo> getProductList(int pageNum,int pageSize);

    //搜索
    ServerResponse<PageInfo> searchProduct(String productName,Integer productId,int pageNum,int pageSize);

    //前台页面的商品详情
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    //通过通过种类和关键字进行分类
    ServerResponse<PageInfo> getProductByKeywordCateory(String keyword,Integer categoryId,int pageNum,int PageSize,String orderBy);
}













