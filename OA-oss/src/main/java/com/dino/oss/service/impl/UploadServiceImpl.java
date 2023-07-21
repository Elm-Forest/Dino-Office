package com.dino.oss.service.impl;

import com.dino.oss.context.UploadStrategyContext;
import com.dino.oss.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
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
