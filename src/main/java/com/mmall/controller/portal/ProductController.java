package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前台页面的一个Productcontroller,前台的一个产品详情
 */
@Controller
@RequestMapping("/product/")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<ProductDetailVo> detail(Integer productId){
        /*
        前台产品详情和后台的产品详情差不多，就一点点的不同之处，不同之处就是在前台查看产品详情的时候，我们要判断一下这个
        产品是不是在线状态，如果不是在线状态，那就不返回了，或者返回一个错误
         */
        return iProductService.getProductDetail(productId);
    }


    //商品列表
    @RequestMapping("List.do")
    @ResponseBody
    public ServerResponse<PageInfo> List(@RequestParam(value="keyword",required=false)String keyword
            ,@RequestParam(value="categoryId",required=false)Integer categoryId
            ,@RequestParam(value="pageNum",defaultValue="1")int pageNum
            ,@RequestParam(value="pageSize",defaultValue="10")int pageSize
            ,@RequestParam(value="orderBy",defaultValue="")String orderBy){
        return iProductService.getProductByKeywordCateory(keyword,categoryId,pageNum,pageSize,orderBy);
    }


}













