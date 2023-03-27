package com.ctgu.document.service;

import com.ctgu.common.models.dto.Result;

/**
 * 回收站服务
 *
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:41
 */
public interface RecycleService {
    /**
     * 查询回收站文档
     *
     * @return Result
     */
    Result<?> selectRecDoc();

    /**
     * 查询回收站文件夹
     *
     * @param docId 文件夹id
     * @return Result
     */
    Result<?> recoverDocument(Long docId);

    /**
     * 恢复文档
     *
     * @param docId 文档id
     * @return Result
     */
    Result<?> removeDocument(Long docId);

    /**
     * 恢复文件夹
     *
     * @param docId 文件夹id
     * @return Result
     */
    Result<?> removeFolder(Long docId);
}
