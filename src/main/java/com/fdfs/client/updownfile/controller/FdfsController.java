package com.fdfs.client.updownfile.controller;

import com.fdfs.client.updownfile.fdfs.Fdfsutil;
import com.fdfs.client.updownfile.service.FastDFSClientWrapper;
import com.fdfs.client.updownfile.service.FdfsFileService;
import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author zhangbingquan
 * @version 2019年08月12日
 * @since 2019年08月12日
 **/
@Controller
@Slf4j
public class FdfsController {
    private final Fdfsutil fdfsutil;
    private  final FastDFSClientWrapper fastDFSClientWrapper;

    @Autowired
    public FdfsController(@Qualifier("myFastFileStorageClient") Fdfsutil fdfsutil,FastDFSClientWrapper fastDFSClientWrapper) {
        this.fdfsutil = fdfsutil;
        this.fastDFSClientWrapper = fastDFSClientWrapper;
    }


    @PostMapping
    public void getImage(){
//        StorePath storePath = fdfsutil.uploadImage(new FastImageFile());
    }

    @RequestMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String fileName = file.getName();
        long fileSize = file.getSize();
        System.out.println(originalFileName + "==========" + fileName + "===========" + fileSize + "===============" + extension + "====" + bytes.length);
        return fastDFSClientWrapper.uploadFile(bytes, fileSize, extension);
    }

    @RequestMapping("/download")
    public void downloadFile(String fileUrl, HttpServletResponse response) throws IOException {
        byte[] bytes = fastDFSClientWrapper.downloadFile(fileUrl);
        // 这里只是为了整合fastdfs，所以写死了文件格式。需要在上传的时候保存文件名。下载的时候使用对应的格式
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode("sb.xlsx", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private FdfsWebServer fdfsWebServer;

    @Autowired
    FdfsFileService fdfsFileService;

    @PutMapping("/article/img/fdfs")
    @ResponseBody
    public String uploadImgfdfs(@RequestParam(value = "editormd-image-file") MultipartFile multipartFile) throws IOException {
        StorePath storePath= storageClient.uploadFile(multipartFile.getInputStream(),
                multipartFile.getSize(), "jpg", null);
        String path = storePath.getFullPath();
        log.info("保存路径={}",path);
        return path;
    }

    @RequestMapping("up")
    @ResponseBody
    public String upload(){
        return "uploadfile";
    }



    @GetMapping("listFile")
    @ResponseBody
    public List<String> getFileURl(){
        return fdfsFileService.getfdfsURL();
    }

}
