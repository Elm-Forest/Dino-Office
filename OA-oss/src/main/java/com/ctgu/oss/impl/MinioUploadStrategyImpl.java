package com.ctgu.oss.impl;

import com.ctgu.oss.config.MinioConfigProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * oss上传策略
 *
 * @author CTGU_LLZ(404name)
 * @date 2021/07/28
 */
@Service("minioUploadStrategyImpl")
@EnableConfigurationProperties(MinioConfigProperties.class)
public class MinioUploadStrategyImpl extends AbstractUploadStrategyImpl {
    @Resource
    private MinioConfigProperties minioConfigProperties;

    @Override
    public boolean delete(String url) {
        throw new NotImplementedException();
    }

    @Override
    public Boolean exists(String filePath) {
        return false;
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream, String contentType) {
        try {
            getMinioClient().putObject(PutObjectArgs.builder()
                    .bucket(minioConfigProperties.getBucketName())
                    .object(path + fileName)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(contentType)
                    .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return minioConfigProperties.getEndpoint() + minioConfigProperties.getBucketName() + '/' + filePath;
    }

    private MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(minioConfigProperties.getEndpoint())
                .credentials(minioConfigProperties.getAccessKey(), minioConfigProperties.getSecretKey())
                .build();
    }


}
