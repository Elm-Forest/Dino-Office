package com.ctgu.oaoss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.ctgu.oaoss.config.OssConfigProperties;
import com.ctgu.oaoss.context.UploadStrategyContext;
import com.ctgu.oaoss.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Zhang Jinming
 * @create 6/6/2022 上午12:30
 */
@Service
public class UploadServiceImpl implements UploadService {
    private final UploadStrategyContext uploadStrategyContext;
    @Autowired
    private OssConfigProperties ossConfigProperties;

    @Autowired
    public UploadServiceImpl(UploadStrategyContext uploadStrategyContext) {
        this.uploadStrategyContext = uploadStrategyContext;
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        return uploadStrategyContext.executeUploadStrategy(file, path);
    }

    @Override
    public String deleteFile(String url) {
        String endpoint = ossConfigProperties.getEndpoint();
        String accessKeyId = ossConfigProperties.getAccessKeyId();
        String accessKeySecret = ossConfigProperties.getAccessKeySecret();
        String bucketName = ossConfigProperties.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, new URL(url).getPath());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }
}
