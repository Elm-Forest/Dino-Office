package com.ctgu.oaschedule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctgu.oacommon.entity.*;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oaschedule.mapper.FriendsMapper;
import com.ctgu.oaschedule.mapper.NoteMapper;
import com.ctgu.oaschedule.mapper.ScheduleDeptMapper;
import com.ctgu.oaschedule.mapper.ScheduleMapper;
import com.ctgu.oaschedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ctgu.oacommon.vo.Result;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author Li Zihan
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private FriendsMapper friendsMapper;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private ScheduleDeptMapper scheduleDeptMapper;

    @Override
    public Result<?> seeSchedule() {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaUpdateWrapper<ScheduleArrange> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScheduleArrange::getUserId, id);
        List<ScheduleArrange> scheduleArrange = scheduleMapper.selectList(updateWrapper);
        return Result.ok(scheduleArrange);
    }

    @Override
    public Result<?> setSchedule(String scheduleTitle, String scheduleContent, Date beginTime, Date endTime) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        Date dateNow = new Date();// 获取当前时间
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaUpdateWrapper<ScheduleArrange> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScheduleArrange::getUserId, id)
                .set(ScheduleArrange::getScheduleTitle, scheduleTitle)
                .set(ScheduleArrange::getScheduleContent, scheduleContent)
                .set(ScheduleArrange::getCreateTime, dateNow)
                .set(ScheduleArrange::getBeginTime, beginTime)
                .set(ScheduleArrange::getEndTime, endTime);
        ScheduleArrange scheduleArrange = new ScheduleArrange();
        int result = scheduleMapper.update(scheduleArrange, updateWrapper);
        if (result <= 0) {
            return Result.fail("更新日程表失败");
        }
        return Result.ok("更新日程表成功");
    }

    @Override
    public Result<?> addFriends(Long friendId) {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        Friends friends = Friends.builder()
                .userId(id)
                .friendId(friendId)
                .build();
        int insert = friendsMapper.insert(friends);
        if (insert == 0) {
            return Result.fail("添加联系人失败");
        }
        return Result.ok("添加联系人成功");
    }

    @Override
    public Result<?> delFriends(Long friendId) {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaUpdateWrapper<Friends> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Friends::getUserId, id)
                .eq(Friends::getFriendId, friendId);
        int del = friendsMapper.delete(updateWrapper);
        if (del == 0) {
            return Result.fail("删除联系人失败");
        }
        return Result.ok("删除联系人成功");
    }

    @Override
    public Result<?> seeFriends(Long friendId) {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaQueryWrapper<Friends> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friends::getUserId, id)
                .eq(Friends::getFriendId, friendId);
        Friends friends = friendsMapper.selectOne(queryWrapper);
        LambdaUpdateWrapper<ScheduleArrange> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScheduleArrange::getUserId, friends.getFriendId());
        List<ScheduleArrange> list = scheduleMapper.selectList(updateWrapper);
        return Result.ok(list);
    }

    @Override
    public Result<?> addNote(String noteContent) {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        Note note = Note.builder()
                .userId(id)
                .noteContent(noteContent)
                .build();
        int insert = noteMapper.insert(note);
        if (insert == 0) {
            return Result.fail("用户添加便签失败");
        }
        return Result.ok("用户添加便签成功");
    }

    @Override
    public Result<?> delNote() {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, id);
        int del = noteMapper.delete(queryWrapper);
        if (del == 0) {
            return Result.fail("用户删除便签失败");
        }
        return Result.ok("用户删除便签成功");
    }

    @Override
    public Result<?> updateNote(String noteContent) {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaUpdateWrapper<Note> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Note::getUserId, id)
                .set(Note::getNoteContent, noteContent);
        Note note = new Note();
        int result = noteMapper.update(note, updateWrapper);
        if (result <= 0) {
            return Result.fail("用户更新便签失败");
        }
        return Result.ok("用户更新便签成功");
    }

    @Override
    public Result<?> findNote() {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getUserId, id);
        Note note = noteMapper.selectOne(queryWrapper);
        return Result.ok(note);
    }

    @Override
    public Result<?> addSchedule() {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        Date dateNow = new Date();// 获取当前时间
        ScheduleArrange scheduleArrange = ScheduleArrange.builder()
                .userId(id)
                .createTime(dateNow)
                .build();
        int insert = scheduleMapper.insert(scheduleArrange);
        if (insert == 0) {
            return Result.fail("用户添加日程表失败");
        }
        return Result.ok("用户添加日程表成功");
    }

    @Override
    public Result<?> delSchedule() {
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaQueryWrapper<ScheduleArrange> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduleArrange::getUserId, id);
        int del = scheduleMapper.delete(queryWrapper);
        if (del == 0) {
            return Result.fail("用户删除日程表失败");
        }
        return Result.ok("用户删除日程表成功");
    }

    @Override
    public Result<?> addScheduleDept(Long deptId) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        Date dateNow = new Date();// 获取当前时间
        ScheduleDepartment scheduleDepartment = ScheduleDepartment.builder()
                .deptId(deptId)
                .createTime(dateNow)
                .build();
        int insert = scheduleDeptMapper.insert(scheduleDepartment);
        if (insert == 0) {
            return Result.fail("添加部门日程表失败");
        }
        return Result.ok("添加部门日程表成功");
    }

    @Override
    public Result<?> delScheduleDept(Long deptId) {
        LambdaQueryWrapper<ScheduleDepartment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduleDepartment::getDeptId, deptId);
        int del = scheduleDeptMapper.delete(queryWrapper);
        if (del == 0) {
            return Result.fail("删除部门日程表失败");
        }
        return Result.ok("删除部门日程表成功");
    }

    @Override
    public Result<?> updateScheduleDept(Long deptId, String scheduleTitle, String scheduleContent, Date beginTime, Date endTime) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        Date dateNow = new Date();// 获取当前时间
        LambdaUpdateWrapper<ScheduleDepartment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScheduleDepartment::getDeptId, deptId)
                .set(ScheduleDepartment::getScheduleTitle, scheduleTitle)
                .set(ScheduleDepartment::getScheduleContent, scheduleContent)
                .set(ScheduleDepartment::getCreateTime, dateNow)
                .set(ScheduleDepartment::getBeginTime, beginTime)
                .set(ScheduleDepartment::getEndTime, endTime);
        ScheduleDepartment scheduleDepartment = new ScheduleDepartment();
        int result = scheduleDeptMapper.update(scheduleDepartment, updateWrapper);
        if (result <= 0) {
            return Result.fail("更新部门日程表失败");
        }
        return Result.ok("更新部门日程表成功");
    }

    @Override
    public Result<?> findScheduleDept(Long deptId) {
        LambdaQueryWrapper<ScheduleDepartment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ScheduleDepartment::getDeptId, deptId);
        List<ScheduleDepartment> list = scheduleDeptMapper.selectList(queryWrapper);
        return Result.ok(list);
    }
}
