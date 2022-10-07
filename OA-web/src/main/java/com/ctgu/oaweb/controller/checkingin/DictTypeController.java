package com.ctgu.oaweb.controller.checkingin;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctgu.oacheckingin.mapper.AttendanceStatisticsMapper;
import com.ctgu.oacommon.entity.AttendanceStatistics;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Li Zihan
 */
@Tag(name = "管理员导出用户考勤表")
@RestController
@RequestMapping("/doc")
public class DictTypeController {
    @Resource
    private AttendanceStatisticsMapper dictTypeMapper;

    @RequestMapping("/outExcel")
    @Operation(summary = "导出用户考勤表", description = "export dict type")
    public void typeExport(HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = "用户考勤表";
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                filename = URLEncoder.encode(filename, StandardCharsets.UTF_8);
            } else {
                filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setContentType("application/json.ms-exce");
            response.setCharacterEncoding("utf-8");
            response.addHeader("Content-Disposition", "filename = " + filename + ".xlsx");
            QueryWrapper<AttendanceStatistics> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("*");
            List<AttendanceStatistics> dictTypeList = dictTypeMapper.selectList(queryWrapper);
            EasyExcel.write(response.getOutputStream(), AttendanceStatistics.class).sheet("sheet").doWrite(dictTypeList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

