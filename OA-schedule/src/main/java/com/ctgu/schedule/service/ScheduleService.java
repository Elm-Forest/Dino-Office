package com.ctgu.schedule.service;

import com.ctgu.common.models.dto.Result;

/**
 * 日程服务
 *
 * @author Li Zihan
 */
public interface ScheduleService {

    /**
     * 查看日程
     *
     * @return Result
     */
    Result<?> seeSchedule();

    /**
     * 查看日程
     *
     * @param scheduleId 日程id
     * @return Result
     */
    Result<?> seeScheduleById(Long scheduleId);

    /**
     * 修改日程
     *
     * @param scheduleId      日程id
     * @param scheduleTitle   日程标题
     * @param scheduleContent 日程内容
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @return Result
     */
    Result<?> setSchedule(Long scheduleId, String scheduleTitle, String scheduleContent, String beginTime, String endTime);


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
     * 设置便签
     *
     * @param noteContent 便签内容
     * @return Result
     */
    Result<?> addNote(String noteContent);

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
     * @param noteContent 便签内容
     * @param noteId      便签id
     * @return Result
     */
    Result<?> updateNote(String noteContent, Long noteId);

    /**
     * 查看便签
     *
     * @param noteId 便签id
     * @return Result
     */
    Result<?> findNoteById(Long noteId);

    /**
     * 查看便签
     *
     * @return Result
     */
    Result<?> findNote();

    /**
     * 添加日程
     *
     * @param scheduleTitle   日程标题
     * @param scheduleContent 日程内容
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @return Result
     */
    Result<?> addSchedule(String scheduleTitle, String scheduleContent, String beginTime, String endTime);

    /**
     * 删除日程
     *
     * @param scheduleId 日程id
     * @return Result
     */
    Result<?> delSchedule(Long scheduleId);

    /**
     * 添加日程
     *
     * @param scheduleTitle   日程标题
     * @param scheduleContent 日程内容
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @return Result
     */
    Result<?> addScheduleDept(String scheduleTitle, String scheduleContent, String beginTime, String endTime);

    /**
     * 删除日程
     *
     * @param scheduleDeptId 日程id
     * @return Result
     */
    Result<?> delScheduleDept(Long scheduleDeptId);

    /**
     * 修改日程
     *
     * @param scheduleDeptId  日程id
     * @param scheduleTitle   日程标题
     * @param scheduleContent 日程内容
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @return Result
     */
    Result<?> updateScheduleDept(Long scheduleDeptId, String scheduleTitle, String scheduleContent, String beginTime, String endTime);

    /**
     * 查看日程
     *
     * @return Result
     */
    Result<?> findScheduleDept();
}
