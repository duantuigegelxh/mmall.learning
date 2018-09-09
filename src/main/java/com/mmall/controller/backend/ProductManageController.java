package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 产品管理模块
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;
    @Autowired
    private IFileService iFileService;

    // 保存商品的接口
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充我们增加产品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }


    //产品上下架,产品的状态,更新产品的销售状态
    @RequestMapping("set_sale.status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus();
        } else {
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }


    //获取产品详情的接口
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }


    //管理后台，关于产品的一个list
    @RequestMapping("List.do")
    @ResponseBody
    public ServerResponse getList(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务,添加一个动态分页
            return IProductService.getProductList(pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }

    //后台商品搜索功能开发
    @RequestMapping("search.do")
    @RequestBody
    public ServerResponse productSearch(HttpSession session, String productName, String productId,, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录管理员");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //填充业务,添加一个动态分页
            return IProductService.searchProduct(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByErrorCodeMessage("无权限操作");
        }
    }


    //图片文件的上传
    @RequestMapping("upload.do")
    @ResponseBody
    //request用来动态创建一个路径出来,为了防止外界使用我们的接口，我们在这里需要做一个权限管理（session）
    public ServerResponse upload(@RequestParam(value="upload_file",required=false) MultipartFile file,HttpServletRequest request,HttpSession session,httpServletResponse response) {

        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆，请登陆管理员");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //request.getSession().getServletContext() 获取的是Servlet容器对象，相当于tomcat容器了。getRealPath("/") 获取
            // 实际路径，“/”指代项目根目录，所以代码返回的是项目在容器中的实际发布运行的根路径,这个路径上传完之后会创建到
            //我们发布之后的webapp，然后和web-info/index.jsp是同级的
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);
            //根据和前端的约定，要把url给ping出来
            String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;//传过来的property属性最后有一个“/”是因为我们的文件名没有，保证这个是正确的
            Map fileMap=Maps.newHashMap();
            fileMap.put("uri",targetFileName);
            fileMap.put("url",url);

            response.addHeader("Access-Control-ALlow_Headers","X-File-Name");//我们需要设置一个头文件的相应，返回成功的，这个是和前端的约定，前端很多的插件需要不同的header，因此我们需要知道怎么出设置header
            return ServerResponse.createBySuccess(fileMap);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }


    //富文本的上传
    @RequestMapping("richtest_img_upload.do")
    @ResponseBody
    //request用来动态创建一个路径出来,为了防止外界使用我们的接口，我们在这里需要做一个权限管理（session）
    public Map richtestImgUpload(@RequestParam(value="upload_file",required=false) MultipartFile file,HttpServletRequest request,HttpSession session) {

        Map resultMap=Maps.newHashMap();

        User user=(User)session.getAttribute(Const.CURRENT_USER);
        if(user==null){
            resultMap.put("success",false);
            resultMap.put("msg","请登陆管理员");
            return resultMap;
        }
        //富文本中对自己的返回值有自己的要求，我们使用的是simditor，所以按照simditor的方式返回，因为我们使用了simditor插件，所以我们需要使用富文本的格式
        /*
          {
              "success":true/false
              "msg":"error message",# optional"
              "file_path":"[real file path]"
          }
         */
        if(iUserService.checkAdminRole(user).isSuccess()){
            //request.getSession().getServletContext() 获取的是Servlet容器对象，相当于tomcat容器了。getRealPath("/") 获取
            // 实际路径，“/”指代项目根目录，所以代码返回的是项目在容器中的实际发布运行的根路径,这个路径上传完之后会创建到
            //我们发布之后的webapp，然后和web-info/index.jsp是同级的
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName=iFileService.upload(file,path);

            //对targetFileName进行null判断
            if(StringUtils.isBlank(targetFileName)){//如果为空
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }

            //根据和前端的约定，要把url给ping出来
            String url=PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;//传过来的property属性最后有一个“/”是因为我们的文件名没有，保证这个是正确的
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);

            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }




}



















