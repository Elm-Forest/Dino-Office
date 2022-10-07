package com.ctgu.oaweb.controller.schedule;

import com.ctgu.oacommon.vo.Result;
import com.ctgu.oaschedule.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Li Zihan
 */
@RestController
@RequestMapping(value = "/note")
@Api(tags = "便签管理")
public class NoteController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/add")
    @ApiOperation(value = "用户添加便签")
    public Result<?> addNote(@RequestParam("noteContent") String noteContent) {
        return scheduleService.addNote(noteContent);
    }

    @DeleteMapping("/del")
    @ApiOperation(value = "用户删除便签")
    public Result<?> delNote() {
        return scheduleService.delNote();
    }

    @PutMapping("/update")
    @ApiOperation(value = "用户修改便签")
    public Result<?> updateNote(@RequestParam("noteContent") String noteContent) {
        return scheduleService.updateNote(noteContent);
    }

    @GetMapping("/find")
    @ApiOperation(value = "用户查看便签")
    public Result<?> findNote() {
        return scheduleService.findNote();
    }
}
