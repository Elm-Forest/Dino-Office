package com.dino.web.controller.schedule;

import com.dino.common.models.dto.Result;
import com.dino.schedule.service.ScheduleService;
import com.dino.common.models.vo.NoteVO;
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
    public Result<?> addNote(NoteVO noteVO) {
        return scheduleService.addNote(noteVO);
    }

    @DeleteMapping
    @ApiOperation(value = "用户删除便签")
    public Result<?> delNote(@RequestParam("noteId") Long noteId) {
        return scheduleService.delNote(noteId);
    }

    @PutMapping
    @ApiOperation(value = "用户修改便签")
    public Result<?> updateNote(NoteVO noteVO) {
        return scheduleService.updateNote(noteVO);
    }

    @GetMapping
    @ApiOperation(value = "用户查看指定便签")
    public Result<?> findNoteById(@RequestParam("noteId") Long noteId) {
        return scheduleService.findNoteById(noteId);
    }

    @GetMapping("list")
    @ApiOperation(value = "用户查看所有便签")
    public Result<?> findNote() {
        return scheduleService.findNote();
    }
}
