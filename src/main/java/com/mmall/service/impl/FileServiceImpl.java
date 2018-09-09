package com.mmall.service.impl;

import com.mmall.service.IFileService;
import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
/**
 *文件上传
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{

    //我们在这里打印一个日志，因为经常会被调用
    private Logger logger=LoggerFactory.getLogger(FileServiceImpl.class);

    public String upload(MultipartFile file,String path){//将上传后的文件名返回过去
        String fileName=file.getOriginalFilename();//获取上传文件的原始文件名
        //扩展名,假如文件名为abc.jpg，我们从后面的.开始获取,就获取到了拓展名
        String fileExtensionName=fileName.substring(fileName.lastIndexOf(".")+1);
        //以后如果A上传了一个abc.jpg,B也上次了一个abc.jpg,会导致重复覆盖，这里可以使用UUID
        String uploadFileName=UUID.randomUUID().toString()+"."+fileExtensionName;
        //将打印的日志输出一下，使用{}进行占位
        logger.info("开始上传文件，上传的文件名是：{},上传的路径：{},上传的新文件名：{}",fileName,path,uploadFileName);

        //目录的文件名
        File fileDir=new File(path);
        if(!fileDir.exists()){//判断这个路径是否存在，如果存在，则返回true
            fileDir.setWritable(true);//赋予一个权限，表示可写
            //mkdirs()可以建立多级文件夹， mkdir()只会建立一级的文件夹， 如下：new File("/tmp/one/two/three").mkdirs();
            // 执行后， 会建立tmp/one/two/three四级目录,new File("/tmp/one/two/three").mkdir();则不会建立任何目录， 因为找不到/tmp/one/two目录， 结果返回false
            fileDir.mkdirs();
        }
        File targetFile=new File(path,uploadFileName); //File(String parent,String child):根据一个目录和一个子文件/目录得到File对象

        try{
            file.transferTo(targetFile);//使用transferTo（dest）方法将上传文件写到服务器上指定的文件
            //倒了这一步，文件上传成功，就已经上传到我们的upload文件夹下了。


            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //将targetFile上传到我们的FTP服务器上

            targetFile.delete();
            //上传完后，删除upload下面的文件


        }catch(IOException e){
           logger.error("上传文件异常",e);
            return null;//返回一个null。说明没有文件名传给它。
        }


        return targetFile.getName();

    }
}















