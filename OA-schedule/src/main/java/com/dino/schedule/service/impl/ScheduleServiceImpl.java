package com.dino.schedule.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.dino.common.dao.schedule.FriendsMapper;
import com.dino.common.dao.schedule.NoteMapper;
import com.dino.common.dao.schedule.ScheduleDeptMapper;
import com.dino.common.dao.schedule.ScheduleMapper;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.entity.*;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.NoteVO;
import com.dino.common.models.vo.ScheduleArrangeVO;
import com.dino.common.models.vo.ScheduleDepartmentVO;
import com.dino.common.utils.Assert;
import com.dino.common.utils.ThreadHolder;
import com.dino.schedule.service.ScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Li Zihan
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Resource
    private ScheduleMapper scheduleMapper;

    @Resource
    private FriendsMapper friendsMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private NoteMapper noteMapper;

    @Resource
    private ScheduleDeptMapper scheduleDeptMapper;

    @Override
    public Result<?> addFriends(Long friendId) {
        User user = ThreadHolder.getCurrentUser();
        Long id = user.getId();
        Friends friends = Friends.builder()
                .userId(id)
                .friendId(friendId)
                .build();
        int insert = friendsMapper.insert(friends);
        if (insert == 0) {
            throw new RuntimeException("添加联系人失败");
        }
        return Result.ok();
    }

    @Override
    public Result<?> delFriends(Long friendId) {
        User user = ThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaUpdateWrapper<Friends> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Friends::getUserId, id)
                .eq(Friends::getFriendId, friendId);
        int del = friendsMapper.delete(updateWrapper);
        if (del == 0) {
            throw new RuntimeException("删除联系人失败");
        }
        return Result.ok();
    }

    @Override
    public Result<?> seeFriends() {
        User user = ThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaQueryWrapper<Friends> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friends::getUserId, id);
        List<Friends> list = friendsMapper.selectList(queryWrapper);
        return Result.ok(list);
    }

    @Override
    public Result<?> seeFriendsSchedule(Long friendId) {
        User user = ThreadHolder.getCurrentUser();
        Long id = user.getId();
        LambdaQueryWrapper<Friends> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friends::getUserId, id)
                .eq(Friends::getFriendId, friendId);
        Friends friends = friendsMapper.selectOne(queryWrapper);
        LambdaUpdateWrapper<ScheduleArrange> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ScheduleArrange::getUserId, friends.getFriendId());
        List<ScheduleArrange> list = scheduleMapper.selectList(updateWrapper);
        Assert.objNotNull(friends, new RuntimeException("并未拥有此联系人的好友，请先添加其为好友"));
        return Result.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> addNote(NoteVO noteVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Note note = Note.builder()
                .userId(id)
                .noteTitle(noteVO.getNoteTitle())
                .noteContent(noteVO.getNoteContent())
                .build();
        int insert = noteMapper.insert(note);
        if (insert == 0) {
            throw new RuntimeException("用户添加便签失败");
        }
        return Result.ok("用户添加便签成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> delNote(Long noteId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        int del = noteMapper.delete(new LambdaQueryWrapper<Note>()
                .eq(Note::getId, noteId)
                .eq(Note::getUserId, id));
        if (del == 0) {
            throw new RuntimeException("用户删除便签失败");
        }
        return Result.ok("用户删除便签成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updateNote(NoteVO noteVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Note note = new Note();
        int result = noteMapper.update(note, new LambdaUpdateWrapper<Note>()
                .eq(Note::getId, noteVO.getId())
                .eq(Note::getUserId, id)
                .set(Note::getNoteTitle, noteVO.getNoteTitle())
                .set(Note::getNoteContent, noteVO.getNoteContent()));
        if (result <= 0) {
            throw new RuntimeException("用户修改便签失败");
        }
        return Result.ok("用户修改便签成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findNoteById(Long noteId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Note note = noteMapper.selectOne(new LambdaQueryWrapper<Note>()
                .eq(Note::getUserId, id)
                .eq(Note::getId, noteId));
        return Result.ok(note);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findNote() {
        Long id = ThreadHolder.getCurrentUser().getId();
        List<Note> list = noteMapper.selectList(new LambdaQueryWrapper<Note>()
                .eq(Note::getUserId, id));
        return Result.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> addSchedule(ScheduleArrangeVO scheduleArrangeVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        DateTime nowTime = DateUtil.date();
        String startTime = scheduleArrangeVO.getStartTime();
        String endTime = scheduleArrangeVO.getEndTime();
        ScheduleArrange scheduleArrange = ScheduleArrange.builder()
                .userId(id)
                .scheduleTitle(scheduleArrangeVO.getScheduleTitle())
                .scheduleContent(scheduleArrangeVO.getScheduleContent())
                .createTime(nowTime)
                .startTime(new DateTime(startTime))
                .endTime(new DateTime(endTime))
                .build();
        int insert = scheduleMapper.insert(scheduleArrange);
        Assert.greaterThanZero(insert, new RuntimeException("添加个人日程表失败"));
        return Result.ok("添加个人日程表成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> delSchedule(Long scheduleId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        int del = scheduleMapper.delete(new LambdaQueryWrapper<ScheduleArrange>()
                .eq(ScheduleArrange::getUserId, id)
                .eq(ScheduleArrange::getId, scheduleId));
        Assert.greaterThanZero(del, new RuntimeException("删除个人日程表失败"));
        return Result.ok("删除个人日程表成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setSchedule(ScheduleArrangeVO scheduleArrangeVO) {
        DateTime nowTime = DateUtil.date();
        String startTime = scheduleArrangeVO.getStartTime();
        String endTime = scheduleArrangeVO.getEndTime();
        ScheduleArrange scheduleArrange = new ScheduleArrange();
        int result = scheduleMapper.update(scheduleArrange, new LambdaUpdateWrapper<ScheduleArrange>()
                .eq(ScheduleArrange::getId, scheduleArrangeVO.getId())
                .set(ScheduleArrange::getScheduleTitle, scheduleArrangeVO.getScheduleTitle())
                .set(ScheduleArrange::getScheduleContent, scheduleArrangeVO.getScheduleContent())
                .set(ScheduleArrange::getCreateTime, nowTime)
                .set(ScheduleArrange::getStartTime, startTime)
                .set(ScheduleArrange::getEndTime, endTime));
        if (result <= 0) {
            throw new RuntimeException("更新个人日程表失败");
        }
        return Result.ok("更新个人日程表成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findSchedule() {
        Long id = ThreadHolder.getCurrentUser().getId();
        List<ScheduleArrange> list = scheduleMapper.selectList(new LambdaUpdateWrapper<ScheduleArrange>()
                .eq(ScheduleArrange::getUserId, id));
        return Result.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findScheduleById(Long scheduleId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        ScheduleArrange scheduleArrange = scheduleMapper.selectOne(new LambdaUpdateWrapper<ScheduleArrange>()
                .eq(ScheduleArrange::getUserId, id)
                .eq(ScheduleArrange::getId, scheduleId));
        return Result.ok(scheduleArrange);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> addScheduleDept(ScheduleDepartmentVO scheduleDepartmentVO) {
        Long deptId = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId()).getDeptId();
        DateTime nowTime = DateUtil.date();
        String startTime = scheduleDepartmentVO.getStartTime();
        String endTime = scheduleDepartmentVO.getEndTime();
        ScheduleDepartment scheduleDepartment = ScheduleDepartment.builder()
                .deptId(deptId)
                .scheduleTitle(scheduleDepartmentVO.getScheduleTitle())
                .scheduleContent(scheduleDepartmentVO.getScheduleContent())
                .startTime(new DateTime(startTime))
                .endTime(new DateTime(endTime))
                .createTime(nowTime)
                .build();
        int insert = scheduleDeptMapper.insert(scheduleDepartment);
        if (insert == 0) {
            throw new RuntimeException("添加部门日程表失败");
        }
        return Result.ok("添加部门日程表成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> delScheduleDept(Long scheduleDeptId) {
        Long deptId = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId()).getDeptId();
        int del = scheduleDeptMapper.delete(new LambdaQueryWrapper<ScheduleDepartment>()
                .eq(ScheduleDepartment::getDeptId, deptId)
                .eq(ScheduleDepartment::getId, scheduleDeptId));
        if (del == 0) {
            throw new RuntimeException("删除部门日程表失败");
        }
        return Result.ok("删除部门日程表成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> updateScheduleDept(ScheduleDepartmentVO scheduleDepartmentVO) {
        Long deptId = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId()).getDeptId();
        DateTime nowTime = DateUtil.date();
        String startTime = scheduleDepartmentVO.getStartTime();
        String endTime = scheduleDepartmentVO.getEndTime();
        ScheduleDepartment scheduleDepartment = new ScheduleDepartment();
        int result = scheduleDeptMapper.update(scheduleDepartment, new LambdaUpdateWrapper<ScheduleDepartment>()
                .eq(ScheduleDepartment::getId, scheduleDepartmentVO.getId())
                .set(ScheduleDepartment::getScheduleTitle, scheduleDepartmentVO.getScheduleTitle())
                .set(ScheduleDepartment::getScheduleContent, scheduleDepartmentVO.getScheduleContent())
                .set(ScheduleDepartment::getCreateTime, nowTime)
                .set(ScheduleDepartment::getStartTime, startTime)
                .set(ScheduleDepartment::getEndTime, endTime));
        if (result <= 0) {
            throw new RuntimeException("更新部门日程表失败");
        }
        return Result.ok("更新部门日程表成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findScheduleDept() {
        Long deptId = userInfoMapper.selectById(ThreadHolder.getCurrentUser().getId()).getDeptId();
        List<ScheduleDepartment> list = scheduleDeptMapper.selectList(new LambdaQueryWrapper<ScheduleDepartment>()
                .eq(ScheduleDepartment::getDeptId, deptId));
        return Result.ok(list);
    }
}
