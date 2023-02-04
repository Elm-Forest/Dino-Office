package com.ctgu.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @create 6/6/2022 上午12:47
 */
public interface UploadService {
    /**
     * 上传文件
     *
     * @param file 文件
     * @param path 文件的上传路径，示例: Demo/
     * @return {@link String} 文件的下载地址
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
