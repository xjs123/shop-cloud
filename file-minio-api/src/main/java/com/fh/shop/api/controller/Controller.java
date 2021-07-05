package com.fh.shop.api.controller;

import com.fh.shop.common.ServerResponse;
import com.fh.shop.util.DateForMat;
import com.fh.shop.util.FileUtil;
import io.minio.MinioClient;
import io.minio.policy.PolicyType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@RequestMapping("/api/files")
@RestController
@CrossOrigin
public class Controller {

    @Value("${minio.endpoint}")
    private String endpoint;//minio服务器的地址
    @Value("${minio.accessKey}")
    private String accessKey;//minio登录的用户名 默认是  minioadmin
    @Value("${minio.secretKey}")
    private String secretKey;//minio的登录的密码 默认市  minioadmin
    @Value("${minio.bucketName}")
    private String bucketName;//存储桶的名字


    @PostMapping("/upload")
    public ServerResponse fileUpload(@RequestParam("file") MultipartFile file){

        try {
            //创建于minio的连接
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secretKey);
            //判断有没有桶
            boolean bucketExists = minioClient.bucketExists(bucketName);
            if(!bucketExists){
                //没有就创建桶
                minioClient.makeBucket(bucketName);
                //设置存储桶的权限
                minioClient.setBucketPolicy(bucketName, "*.*", PolicyType.READ_ONLY);
            }
            //以当前日期 yyyy-mm-dd生成文件夹名
            String fileName = DateForMat.date2str(new Date(), DateForMat.Date_Str_Y_M_D);
            //uuid当作文件名
            String imageName = UUID.randomUUID().toString();
            //获取文件的后缀名
            String lastIndexOf = FileUtil.getSuffix(file.getOriginalFilename());
            //进行上传
            minioClient.putObject(bucketName,fileName+"/"+imageName+lastIndexOf,file.getInputStream(),file.getContentType());
            // 相应给前端的路径
            String data=endpoint+"/"+bucketName+"/"+fileName+"/"+imageName+lastIndexOf;
            return ServerResponse.success(data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    //http://192.168.81.137:9090/fh2008/2021-06-24/77e3b44b-93d6-4a90-96ea-0f1a5246d074.jpeg
    public ServerResponse delete(@RequestParam("objectName") String objectName) {
        try {
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secretKey);
           String objName=objectName.replace(endpoint+"/"+bucketName+"/","");

            minioClient.removeObject(bucketName, objName);
            return ServerResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ServerResponse.error();
    }
}
