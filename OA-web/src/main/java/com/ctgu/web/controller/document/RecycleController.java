package com.ctgu.web.controller.document;

import com.ctgu.common.models.dto.Result;
import com.ctgu.document.service.RecycleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午5:41
 */
@Api(tags = "回收站管理")
@RestController
@RequestMapping("/doc/rec")
public class RecycleController {
    @Resource
    private RecycleService recycleService;

    @GetMapping
    public Result<?> selectRecDoc() {
        return recycleService.selectRecDoc();
    }

    @PostMapping
    public Result<?> recoverDocument(@RequestParam("docId") Long docId) {
        return recycleService.recoverDocument(docId);
    }

    @DeleteMapping
    public Result<?> removeDocument(@RequestParam("docId") Long docId) {
        return recycleService.removeDocument(docId);
    }

    @DeleteMapping("/folder")
    public Result<?> removeFolder(@RequestParam("docId") Long docId) {
        return recycleService.removeFolder(docId);
    }
}
