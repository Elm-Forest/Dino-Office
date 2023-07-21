package com.dino.web.controller.checking;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dino.common.dao.checking.CheckMapper;
import com.dino.common.entity.Check;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Li Zihan
 */
@Slf4j
@Api(tags = "管理员导出用户考勤表")
@RestController
@RequestMapping("/check")
public class DictTypeController {
    @Resource
    private CheckMapper dictTypeMapper;

    @GetMapping(value = "/admin/excel", produces = "application/vnd.ms-excel")
    @Operation(summary = "导出用户考勤表", description = "export dict type")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = "用户考勤表";
            String ieAgent1 = "MSIE";
            String ieAgent2 = "Trident";
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains(ieAgent1) || userAgent.contains(ieAgent2)) {
                filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            } else {
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "filename=" + filename + ".xlsx");

            QueryWrapper<Check> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("*");
            List<Check> dictTypeList = dictTypeMapper.selectList(queryWrapper);

            try (OutputStream outputStream = response.getOutputStream()) {
                EasyExcel.write(outputStream, Check.class).sheet("sheet").doWrite(dictTypeList);
            }
        } catch (IOException e) {
            log.error("导出用户考勤表时发生IO异常", e);
        }
    }
}

