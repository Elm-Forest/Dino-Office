package com.ctgu.oacommon.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

/**
 * @author Zhang Jinming
 * &#064;create  1/6/2022 下午11:09
 */
@Configuration
@Data
public class OSSManager implements ProgressListener {
    private long bytesWritten = 0;
    private long totalBytes = -1;
    private boolean succeed = false;

    @Value("${oss.url}")
    private String URL;
    @Value("${oss.endpoint}")
    private String ENDPOINT;
    @Value("${oss.accessKeyId}")
    private String ACCESS_KEY_ID;
    @Value("${oss.accessKeySecret}")
    private String ACCESS_KEY_SECRET;
    @Value("${oss.bucketName}")
    private String BUCKET_NAME;

    /*static {
        InputStream resourceAsStream = OSSManager.class.getClassLoader().getResourceAsStream("oss.properties");
        Properties properties = new Properties();
        try {
            properties.load(resourceAsStream);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "静态资源不存在");
        }
        ENDPOINT = properties.getProperty("endpoint");
        ACCESS_KEY_ID = properties.getProperty("accessKeyId");
        ACCESS_KEY_SECRET = properties.getProperty("accessKeySecret");
        BUCKET_NAME = properties.getProperty("bucketName");
    }*/

    public void upLoadFile(String objectName, InputStream inputStream) {
        String endpoint = "https://" + this.ENDPOINT;
        OSS ossClient;
        try {
            ossClient = new OSSClientBuilder().build(endpoint, this.ACCESS_KEY_ID, this.ACCESS_KEY_SECRET);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("NullPointerException");
            return;
        }
        try {
            ossClient.putObject(new PutObjectRequest(this.BUCKET_NAME, objectName, inputStream).withProgressListener(new OSSManager()));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, " + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered " + "a serious internal problem while trying to communicate with OSS, " + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    public boolean isSucceed() {
        return succeed;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                System.out.println("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                    System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                } else {
                    System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    public String getFileURL(String filePath) {
        return "https://" + this.BUCKET_NAME + "." + this.ENDPOINT + "/" + filePath;
    }
}
