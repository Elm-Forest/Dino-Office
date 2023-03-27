package com.ctgu.document.service;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DocLogConditionVO;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
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
