package com.ctgu.oss.context;

import com.ctgu.common.enums.UploadModeEnum;
import com.ctgu.oss.UploadStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author Elm Forest
 */
@Service
public class UploadStrategyContext {
    /**
     * 上传模式
     */
    @Value("${upload.mode}")
    private String uploadMode;

    @Resource
    private Map<String, UploadStrategy> uploadStrategyMap;

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 路径
     * @return {@link String} 文件地址
     */
    public String executeUploadStrategy(MultipartFile file, String path) {
        return uploadStrategyMap.get(UploadModeEnum.getStrategy(uploadMode)).uploadFile(file, path);
    }

    /**
     * 删除文件
     *
     * @param path 路径
     * @return {@link String} 文件地址
     */
    public Boolean executeDeleteStrategy(String path) {
        return uploadStrategyMap.get(UploadModeEnum.getStrategy(uploadMode)).deleteFile(path);
    }

}
