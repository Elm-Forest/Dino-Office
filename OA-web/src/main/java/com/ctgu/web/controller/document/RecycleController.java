package com.ctgu.web.controller.document;

import com.ctgu.common.models.dto.Result;
import com.ctgu.document.service.RecycleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 回收站管理
 *
 * @author Zhang Jinming
 * @date 18/8/2022 下午5:41
 */
@Api(tags = "回收站管理")
@RestController
@RequestMapping("/doc/rec")
public class RecycleController {
    @Resource
    private RecycleService recycleService;

    @ApiOperation(value = "查询回收站文件")
    @GetMapping
    public Result<?> selectRecDoc() {
        return recycleService.selectRecDoc();
    }

    @ApiOperation(value = "查询回收站文件夹")
    @PostMapping
    public Result<?> recoverDocument(@RequestParam("docId") Long docId) {
        return recycleService.recoverDocument(docId);
    }

    @ApiOperation(value = "恢复文件")
    @DeleteMapping
    public Result<?> removeDocument(@RequestParam("docId") Long docId) {
        return recycleService.removeDocument(docId);
    }

    @ApiOperation(value = "恢复文件夹")
    @DeleteMapping("/folder")
    public Result<?> removeFolder(@RequestParam("docId") Long docId) {
        return recycleService.removeFolder(docId);
    }
}
