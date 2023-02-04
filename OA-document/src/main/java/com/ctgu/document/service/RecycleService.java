package com.ctgu.document.service;

import com.ctgu.common.models.dto.Result;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:41
 */
public interface RecycleService {
    Result<?> selectRecDoc();

    Result<?> recoverDocument(Long docId);

    Result<?> removeDocument(Long docId);

    Result<?> removeFolder(Long docId);
}
