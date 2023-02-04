package com.ctgu.oss.service.impl;

import com.ctgu.oss.context.UploadStrategyContext;
import com.ctgu.oss.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @create 6/6/2022 上午12:30
 */
@Service
public class UploadServiceImpl implements UploadService {
    private final UploadStrategyContext uploadStrategyContext;

    @Autowired
    public UploadServiceImpl(UploadStrategyContext uploadStrategyContext) {
        this.uploadStrategyContext = uploadStrategyContext;
    }

    @Override
    public String uploadFile(MultipartFile file, String path) {
        return uploadStrategyContext.executeUploadStrategy(file, path);
    }

    @Override
    public Boolean deleteFile(String url) {
        return uploadStrategyContext.executeDeleteStrategy(url);
    }
}
