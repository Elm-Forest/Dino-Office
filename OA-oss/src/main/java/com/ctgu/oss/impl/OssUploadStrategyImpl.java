package com.ctgu.oss.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.ctgu.oss.config.OssConfigProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * @author Elm Forest
 */
@Service("ossUploadStrategyImpl")
@Log4j2
public class OssUploadStrategyImpl extends AbstractUploadStrategyImpl {
    @Resource
    private OssConfigProperties ossConfigProperties;

    @Override
    public boolean delete(String url) {
        String msg;
        OSS ossClient = new OSSClientBuilder()
                .build(ossConfigProperties.getEndpoint(),
                        ossConfigProperties.getAccessKeyId(),
                        ossConfigProperties.getAccessKeySecret());
        try {
            ossClient.deleteObject(ossConfigProperties.getBucketName(), new URL(url).getPath());
            ossClient.shutdown();
            return true;
        } catch (OSSException oe) {
            msg = "OSS服务异常，请求已发送至OSS服务器，但由于某种原因对方拒绝了请求";
            log.error(msg);
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            throw new RuntimeException(msg);
        } catch (ClientException ce) {
            msg = "OSS客户端无法发送请求，请检查本地网络服务或本地配置文件后重试";
            log.error(msg);
            log.error("Error Message:" + ce.getMessage());
            throw new RuntimeException(msg);
        } catch (MalformedURLException e) {
            msg = "不可预料的错误";
            log.error(msg);
            log.error("Error Message:" + e.getMessage());
            throw new RuntimeException(msg);
        } finally {
            ossClient.shutdown();
        }
    }

    @Override
    public Boolean exists(String filePath) {
        return getOssClient().doesObjectExist(ossConfigProperties.getBucketName(), filePath);
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream, String contentType) {
        getOssClient().putObject(ossConfigProperties.getBucketName(), path + fileName, inputStream);
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return ossConfigProperties.getUrl() + filePath;
    }

    /**
     * 获取ossClient
     *
     * @return {@link OSS} ossClient
     */
    private OSS getOssClient() {
        return new OSSClientBuilder().build(ossConfigProperties.getEndpoint(), ossConfigProperties.getAccessKeyId(), ossConfigProperties.getAccessKeySecret());
    }

}
