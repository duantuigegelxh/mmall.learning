package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
/**
 *FTP的配置文件
 */
public class FTPUtil {

    //声明一个日志，用来打印消息
    private static final Logger logger=LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser=PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass=PropertiesUtil.getProperty("ftp.pass");


    public FTPUtil(String ip,int port,String user,String pwd){
        this.ip=ip;
        this.port=port;
        this.pwd=pwd;
        this.user=user;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException{//返回文件是否上传成功
        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);//21是端口，这个是固定的

        logger.info("开始连接ftp服务器");
        boolean result=ftpUtil.uploadFile("img",fileList);

        logger.info("开始连接服务器，结束上传，上传结果：{}");
        return result;
    }


    //这个方法是上传的一个具体逻辑，对外暴露的是上面的public方法
    private  boolean uploaFile(String remotePath,List<File> fileList) throws IOException{//一个是远程的一个路径，一个是file类型的list
        boolean uploaded=true;
        FileInputStream fis=null;
        //连接ftp服务器
        if(connectServer(this.ip,this.port,this.user,this.pwd)){
            try{
                ftpClient.changeWorkingDirectory(remotePath);//这里是切换工作目录，要看remotePath是否为空，如果为空，也就不会切换
                //设置缓冲区
                ftpClient.setBufferSize(1024);
                //设置我们的编码格式
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);//这个将文件类型设置成二进制的文件类型，这个会防止一些乱码的问题
                ftpClient.enterLocalPassiveMode();//打开FTP的被动模式
                for(File fileItem:fileList){
                    fis=new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
            }catch(Exception e){
                logger.error("上传文件异常");
                uploaded=false;
                e.printStackTrace();
            }finally{
                fis.close();
                ftpClient.disconnect();
            }

        }
    }

    private boolean connectServer(String ip,int port,String user,String pwd){

        boolean isSuccess=false;
        ftpClient =new FTPClient();
        try{
            ftpClient.connect(ip);
            isSuccess=ftpClient.login(user,pwd);
        }catch(IOException e){
            logger.error("连接服务器异常");
            e.printStackTrace();
        }
        return isSuccess;
    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public static String getFtpIp() {
        return ftpIp;
    }

    public static void setFtpIp(String ftpIp) {
        FTPUtil.ftpIp = ftpIp;
    }

    public static String getFtpUser() {
        return ftpUser;
    }

    public static void setFtpUser(String ftpUser) {
        FTPUtil.ftpUser = ftpUser;
    }

    public static String getFtpPass() {
        return ftpPass;
    }

    public static void setFtpPass(String ftpPass) {
        FTPUtil.ftpPass = ftpPass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}

