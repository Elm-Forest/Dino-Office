package com.ctgu.schedule.service;

import com.ctgu.common.models.dto.Result;

import java.util.Date;

/**
 * @author Li Zihan
 */
public interface ScheduleService {
    Result<?> seeSchedule();

    Result<?> setSchedule(String scheduleTitle, String scheduleContent, Date beginTime, Date endTime);

    Result<?> addFriends(Long friendId);

    Result<?> delFriends(Long friendId);

    Result<?> seeFriends(Long friendId);

    Result<?> addNote(String noteContent);

    Result<?> delNote();

    Result<?> updateNote(String noteContent);

    Result<?> findNote();

    Result<?> addSchedule();

    Result<?> delSchedule();

    Result<?> addScheduleDept(Long deptId);

    Result<?> delScheduleDept(Long deptId);

    Result<?> updateScheduleDept(Long deptId, String scheduleTitle, String scheduleContent, Date beginTime, Date endTime);

    Result<?> findScheduleDept(Long deptId);
}
