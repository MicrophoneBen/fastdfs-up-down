package com.fdfs.client.updownfile.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zhangbingquan
 * @version 2019年08月21日
 * @since 2019年08月21日
 **/
@Service
@Data
public class FdfsFileService {
    @Value("${fdfs.filename}")
    private String filename;

    @Value("${fdfs.trackerd.ip}")
    private String ip;
    @Value("${fdfs.nginx.port}")
    private String port;

    //遍历文件夹，取出所有文件
    private List<String> getfdfsFile() {
        List<String> files = new ArrayList<>();
        String[] list = getFile(this.filename);
        if (null != list) {
            files.addAll(Arrays.asList(list));
        }
        return files;
    }

    //重载一个返回String[] 类型的
    private String[] getFile(String filename) {
        File listFile = new File(filename);
        String[] fileList;

        if (listFile.exists()) {
            fileList = listFile.list();
        } else {
            throw new RuntimeException("不存在此路径！！！");
        }
        return fileList;
    }


/*
    public static void main(String[] args) {
        FdfsFileService fdfsFileService = new FdfsFileService();
        List<String> files = fdfsFileService.getfdfsURL();
        for (String file : files) {
            System.out.println(file);
        }
    }
*/

    //返回当前组别，这里写死，但实际上应该使用嗅探机制去获取组别，这里留一个接口以供扩展
    private String getGroup(){
        return "group1";
    }

    //返回当前Fdfs文件夹路径，这里写死，但实际上应该使用递归去获取文件夹，这里留一个接口以供扩展
    private String getPrefixDir(){
        return "M00/00/00/";
    }

    //文件封装，返回可访问图片文件
    public List<String> getfdfsURL() {
        List<String> files = new ArrayList<>();
        String[] list =  getUrl(getFile(this.filename));
        if (null != list) {
            for(String s : list){
                files.add("http://" + getIp() +":" + getPort() + "/" + s);
            }
        }
        return files;
    }

    //重载一个返回String[] 类型的
    private String[] getUrl(String[] filename) {
        String urlPrefix = getGroup() + "/" + getPrefixDir();
        String[] urlFile;
        if(null != filename) {
            urlFile = new String[filename.length];
            for (int i = 0; i < filename.length; i++) {
                urlFile[i] = urlPrefix + filename[i];
            }
        }else {
            throw new RuntimeException("Fdfs中没有文件");
        }
        return urlFile;
    }

}
