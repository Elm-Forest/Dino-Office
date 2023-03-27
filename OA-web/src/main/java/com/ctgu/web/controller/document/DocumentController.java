package com.ctgu.web.controller.document;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DocRenameVO;
import com.ctgu.common.models.vo.DocumentConditionVO;
import com.ctgu.document.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 文档管理
 *
 * @author Zhang Jinming
 * @date 17/8/2022 下午7:23
 */
@Api(tags = "文档管理")
@RequestMapping("/doc")
@RestController
public class DocumentController {
    @Resource
    private DocumentService documentService;

    @ApiOperation(value = "查询文档")
    @GetMapping
    public Result<?> selectDocument(@RequestParam("filePath") String filePath) {
        return documentService.selectDocument(filePath);
    }

    @ApiOperation(value = "上传文档")
    @PostMapping
    public Result<?> uploadFile(@RequestParam("file") MultipartFile multipartFile,
                                @RequestParam("filePath") String filePath) {
        return documentService.insertDocument(multipartFile, filePath);
    }

    @ApiOperation(value = "重命名文档")
    @PutMapping("/name")
    public Result<?> renameFile(DocRenameVO docRenameVO) {
        return documentService.renameDocument(docRenameVO);
    }

    @ApiOperation(value = "创建文件夹")
    @PostMapping("/folder")
    public Result<?> createFolder(@RequestParam("filePath") String filePath,
                                  @RequestParam("name") String name) {
        return documentService.createFolder(filePath, name);
    }

    @ApiOperation(value = "删除文档")
    @DeleteMapping
    public Result<?> deleteDocument(@RequestParam("docId") Long docId) {
        return documentService.deleteDocument(docId);
    }

    @ApiOperation(value = "删除文件夹")
    @DeleteMapping("/folder")
    public Result<?> deleteFolder(@RequestParam("docId") Long docId) {
        return documentService.deleteFolder(docId);
    }

    @ApiOperation(value = "查询文档条件")
    @GetMapping("/list")
    public Result<?> selectDocumentCondition(DocumentConditionVO documentConditionVO) {
        return documentService.selectDocumentCondition(documentConditionVO);
    }

    @ApiOperation(value = "下载文档")
    @GetMapping("/download")
    public Result<?> downloadDocument(Long docId) {
        return documentService.downloadFile(docId);
    }

    @ApiOperation(value = "查询文件夹大小")
    @GetMapping("/folder/size")
    public Result<?> selectFolderSize(Long docId) {
        return documentService.selectFolderSize(docId);
    }
}
