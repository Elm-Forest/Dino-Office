package com.dino.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author Elm Forest
 */
@Data
@Component
@ConfigurationProperties(prefix = "upload.minio")
public class
MinioConfigProperties {

    /**
     * 端点
     */
    private String endpoint;
    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName;
}
