package com.ctgu.web.controller.document;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DocLogConditionVO;
import com.ctgu.document.service.DocumentLogService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午8:50
 */
@Api(tags = "文件日志管理")
@RestController
@RequestMapping("/doc/log")
public class DocumentLogController {
    @Resource
    private DocumentLogService documentLogService;

    @GetMapping("/doc/log")
    public Result<?> selectDocument(DocLogConditionVO docLogVO) {
        return documentLogService.selectDocumentLog(docLogVO);
    }
}
