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
    public Result<?> delNote(@RequestParam("noteId") Long noteId) {
        return scheduleService.delNote(noteId);
    }

    @PutMapping
    @ApiOperation(value = "用户修改便签")
    public Result<?> updateNote(@RequestParam("noteContent") String noteContent,
                                @RequestParam("noteId") Long noteId) {
        return scheduleService.updateNote(noteContent,noteId);
    }

    @GetMapping("/find/one")
    @ApiOperation(value = "用户查看指定便签")
    public Result<?> findNoteById(@RequestParam("noteId") Long noteId) {
        return scheduleService.findNoteById(noteId);
    }

    @GetMapping("/find/list")
    @ApiOperation(value = "用户查看所有便签")
    public Result<?> findNote() {
        return scheduleService.findNote();
    }
}
