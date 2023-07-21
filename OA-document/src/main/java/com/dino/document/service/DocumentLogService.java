package com.dino.document.service;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DocLogConditionVO;

/**
 * @author Zhang Jinming
 */
public interface DocumentLogService {
    /**
     * 查询文档日志
     *
     * @param docLogConditionVO 查询条件
     * @return Result
     */
    Result<?> selectDocumentLog(DocLogConditionVO docLogConditionVO);
}
