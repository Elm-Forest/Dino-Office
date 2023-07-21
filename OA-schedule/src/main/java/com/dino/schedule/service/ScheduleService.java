package com.dino.schedule.service;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.NoteVO;
import com.dino.common.models.vo.ScheduleArrangeVO;
import com.dino.common.models.vo.ScheduleDepartmentVO;

/**
 * 日程服务
 *
 * @author Li Zihan
 */
public interface ScheduleService {

    /**
     * 添加个人日程
     *
     * @return Result
     */
    Result<?> addSchedule(ScheduleArrangeVO scheduleArrangeVO);

    /**
     * 删除个人日程
     *
     * @param scheduleId 日程id
     * @return Result
     */
    Result<?> delSchedule(Long scheduleId);

    /**
     * 修改个人日程
     *
     * @return Result
     */
    Result<?> setSchedule(ScheduleArrangeVO scheduleArrangeVO);

    /**
     * 查看所有个人日程
     *
     * @return Result
     */
    Result<?> findSchedule();

    /**
     * 查看指定个人日程
     *
     * @param scheduleId 日程id
     * @return Result
     */
    Result<?> findScheduleById(Long scheduleId);

    /**
     * 添加好友
     *
     * @param friendId 好友id
     * @return Result
     */
    Result<?> addFriends(Long friendId);

    /**
     * 删除好友
     *
     * @param friendId 好友id
     * @return Result
     */
    Result<?> delFriends(Long friendId);

    /**
     * 查看好友
     *
     * @return Result
     */
    Result<?> seeFriends();

    /**
     * 查看好友日程
     *
     * @param friendId 好友id
     * @return Result
     */
    Result<?> seeFriendsSchedule(Long friendId);

    /**
     * 添加便签
     *
     * @return Result
     */
    Result<?> addNote(NoteVO noteVO);

    /**
     * 删除便签
     *
     * @param noteId 便签id
     * @return Result
     */
    Result<?> delNote(Long noteId);

    /**
     * 修改便签
     *
     * @return Result
     */
    Result<?> updateNote(NoteVO noteVO);

    /**
     * 查看指定便签
     *
     * @param noteId 便签id
     * @return Result
     */
    Result<?> findNoteById(Long noteId);

    /**
     * 查看所有便签
     *
     * @return Result
     */
    Result<?> findNote();

    /**
     * 添加部门日程
     *
     * @return Result
     */
    Result<?> addScheduleDept(ScheduleDepartmentVO scheduleDepartmentVO);

    /**
     * 删除部门日程
     *
     * @param scheduleDeptId 日程id
     * @return Result
     */
    Result<?> delScheduleDept(Long scheduleDeptId);

    /**
     * 修改部门日程
     *
     * @return Result
     */
    Result<?> updateScheduleDept(ScheduleDepartmentVO scheduleDepartmentVO);

    /**
     * 查看部门日程
     *
     * @return Result
     */
    Result<?> findScheduleDept();
}
