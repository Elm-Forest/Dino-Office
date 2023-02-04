package com.ctgu.web.controller.checking;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctgu.common.dao.checking.AttendanceStatisticsMapper;
import com.ctgu.common.entity.AttendanceStatistics;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
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
@Api(tags = "管理员导出用户考勤表")
@RestController
@RequestMapping("/doc")
public class DictTypeController {
    @Resource
    private AttendanceStatisticsMapper dictTypeMapper;

    @RequestMapping("/admin/excel")
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

