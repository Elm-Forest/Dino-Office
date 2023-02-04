package com.ctgu.document.service;

import com.ctgu.common.models.vo.DocLogConditionVO;
import com.ctgu.common.models.dto.Result;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
 */
public interface DocumentLogService {
    Result<?> selectDocumentLog(DocLogConditionVO docLogConditionVO);
}
