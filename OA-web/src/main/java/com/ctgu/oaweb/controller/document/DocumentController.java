package com.ctgu.oaweb.controller.document;

import com.ctgu.oacommon.vo.DocumentConditionVO;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadocument.service.DocumentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 下午7:23
 */
@Api("文档管理")
@RequestMapping("/doc")
@RestController
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @GetMapping
    public Result<?> selectDocument(@RequestParam("filePath") String filePath) {
        return documentService.selectDocument(filePath);
    }

    @PostMapping
    public Result<?> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                @RequestParam("filePath") String filePath) {
        return documentService.insertDocument(multipartFile, filePath);
    }

    @PutMapping("/name")
    public Result<?> reNameFile(@RequestParam("rename") String reName,
                                @RequestParam("docId") Long docId,
                                @RequestParam("type") Integer type) {
        return documentService.reNameDocument(docId, reName,type);
    }

    @PostMapping("/folder")
    public Result<?> createFolder(@RequestParam("filePath") String filePath,
                                  @RequestParam("name") String name) {
        return documentService.createFolder(filePath, name);
    }

    @DeleteMapping
    public Result<?> deleteDocument(@RequestParam("docId") Long docId) {
        return documentService.deleteDocument(docId);
    }

    @DeleteMapping("/folder")
    public Result<?> deleteFolder(@RequestParam("docId") Long docId) {
        return documentService.deleteFolder(docId);
    }

    @GetMapping("/list")
    public Result<?> selectDocumentCondition(DocumentConditionVO documentConditionVO) {
        return documentService.selectDocumentCondition(documentConditionVO);
    }
}
