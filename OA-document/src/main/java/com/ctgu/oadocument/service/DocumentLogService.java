package com.ctgu.oadocument.service;

import com.ctgu.oacommon.vo.DocLogConditionVO;
import com.ctgu.oacommon.vo.Result;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
 */
public interface DocumentLogService {
    Result<?> selectDocumentLog(DocLogConditionVO docLogConditionVO);
}
