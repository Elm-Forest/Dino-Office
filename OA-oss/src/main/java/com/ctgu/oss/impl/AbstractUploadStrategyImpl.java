package com.ctgu.oss.impl;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ctgu.common.exception.BizException;
import com.ctgu.oss.UploadStrategy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * 抽象上传模板
 *
 * @author CTGU_LLZ(404name)
 * @date 2021/07/28
 */
@Service
public abstract class AbstractUploadStrategyImpl implements UploadStrategy {

    @Override
    public String uploadFile(MultipartFile file, String path) {
        try {
            // 获取文件md5值
            String md5 = DigestUtil.md5Hex(file.getInputStream());
            // 获取文件扩展名
            String extName = FileNameUtil.extName(file.getOriginalFilename());
            // 重新生成文件名
            String fileName = md5 + extName;
            // 判断文件是否已存在
            if (!exists(path + fileName)) {
                // 不存在则继续上传
                upload(path, fileName, file.getInputStream(), file.getContentType());
            }
            // 返回文件访问路径
            return getFileAccessUrl(path + fileName);
        } catch (BizException e) {
            throw new BizException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Boolean deleteFile(String url) {
        if (exists(url)) {
            throw new BizException("url:" + url + "不存在");
        }
        return delete(url);
    }

    /**
     * 删除文件
     *
     * @param url 文件url
     * @return {@link Boolean} 旗帜
     */
    public abstract boolean delete(String url);

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return {@link Boolean}
     */
    public abstract Boolean exists(String filePath);

    /**
     * 上传
     *
     * @param path        路径
     * @param fileName    文件名
     * @param inputStream 输入流
     * @param contentType ??
     * @throws IOException io异常
     */
    public abstract void upload(String path, String fileName, InputStream inputStream, String contentType) throws IOException;

    /**
     * 获取文件访问url
     *
     * @param filePath 文件路径
     * @return {@link String}
     */
    public abstract String getFileAccessUrl(String filePath);

}
