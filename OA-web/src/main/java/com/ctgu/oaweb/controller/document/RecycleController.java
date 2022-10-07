package com.ctgu.oaweb.controller.document;

import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadocument.service.RecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午5:41
 */
@RestController
public class RecycleController {
    @Autowired
    private RecycleService recycleService;

    @GetMapping("/doc/rec")
    public Result<?> selectRecDoc() {
        return recycleService.selectRecDoc();
    }

    @PostMapping("/doc/rec")
    public Result<?> recoverDocument(@RequestParam("docId") Long docId) {
        return recycleService.recoverDocument(docId);
    }

    @DeleteMapping("/doc/rec")
    public Result<?> removeDocument(@RequestParam("docId") Long docId) {
        return recycleService.removeDocument(docId);
    }

    @DeleteMapping("/doc/rec/folder")
    public Result<?> removeFolder(@RequestParam("docId") Long docId) {
        return recycleService.removeFolder(docId);
    }
}
