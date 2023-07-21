package com.dino.oss;

import org.springframework.web.multipart.MultipartFile;

/**
 * 上传策略
 *
 * @author Elm Forest
 */
public interface UploadStrategy {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 上传路径
     * @return {@link String} 文件地址
     */
    String uploadFile(MultipartFile file, String path);

    /**
     * 删除文件
     *
     * @param url 文件url
     * @return {@link Boolean} 旗帜
     */
    Boolean deleteFile(String url);

}
