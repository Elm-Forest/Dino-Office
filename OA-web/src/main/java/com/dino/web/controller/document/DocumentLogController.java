package com.dino.web.controller.document;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DocLogConditionVO;
import com.dino.document.service.DocumentLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Api(tags = "文件日志管理")
@RestController
@RequestMapping("/doc/log")
public class DocumentLogController {
    @Resource
    private DocumentLogService documentLogService;

    @ApiOperation(value = "查询文件日志")
    @GetMapping
    public Result<?> selectDocument(DocLogConditionVO docLogVO) {
        return documentLogService.selectDocumentLog(docLogVO);
    }
}
