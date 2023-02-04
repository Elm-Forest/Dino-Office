package com.ctgu.web.controller.schedule;

import com.ctgu.common.models.dto.Result;
import com.ctgu.schedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Li Zihan
 */
@Api(tags = "便签管理")
@RestController
@RequestMapping(value = "/note")
public class NoteController {

    @Resource
    private ScheduleService scheduleService;

    @PostMapping
    @ApiOperation(value = "用户添加便签")
    public Result<?> addNote(@RequestParam("noteContent") String noteContent) {
        return scheduleService.addNote(noteContent);
    }

    @DeleteMapping
    @ApiOperation(value = "用户删除便签")
    public Result<?> delNote() {
        return scheduleService.delNote();
    }

    @PutMapping
    @ApiOperation(value = "用户修改便签")
    public Result<?> updateNote(@RequestParam("noteContent") String noteContent) {
        return scheduleService.updateNote(noteContent);
    }

    @GetMapping
    @ApiOperation(value = "用户查看便签")
    public Result<?> findNote() {
        return scheduleService.findNote();
    }
}
