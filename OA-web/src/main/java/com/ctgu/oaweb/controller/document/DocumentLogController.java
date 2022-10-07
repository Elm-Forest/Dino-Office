package com.ctgu.oaweb.controller.document;

import com.ctgu.oacommon.vo.DocLogConditionVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadocument.service.DocumentLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午8:50
 */
@RestController
public class DocumentLogController {
    @Autowired
    private DocumentLogService documentLogService;

    @GetMapping("/doc/log")
    public Result<?> selectDocument(DocLogConditionVO docLogVO) {
        return documentLogService.selectDocumentLog(docLogVO);
    }
}
