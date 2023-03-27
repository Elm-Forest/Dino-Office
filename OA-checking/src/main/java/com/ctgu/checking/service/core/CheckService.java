package com.ctgu.checking.service.core;

import com.ctgu.common.models.dto.Result;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Li Zihan
 */
public interface CheckService {
    /**
     * 签到
     *
     * @return Result
     * @throws ParseException parseException
     */
    Result<?> signIn() throws ParseException;

    /**
     * 签退
     *
     * @return Result
     * @throws ParseException parseException
     */
    Result<?> signOut() throws ParseException;

    /**
     * 查看考勤
     *
     * @param userId 用户id
     * @return Result
     */
    Result<?> findStatisticsById(Long userId);

    /**
     * 设置考勤时间
     *
     * @param startTime 上班时间
     * @param backTime  下班时间
     * @return Result
     */
    Result<?> setAttendanceRole(Date startTime, Date backTime);

    /**
     * 查看考勤
     *
     * @return Result
     */
    Result<?> findStatistics();

    /**
     * 设置节假日
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return Result
     */
    Result<?> setHoliday(Date beginTime, Date endTime);

    /**
     * 设置节假日
     *
     * @param userId 用户id
     * @return Result
     */
    Result<?> setHolidayById(Long userId);

    /**
     * 设置节假日
     */
    void setStartTime();

    /**
     * 设置节假日
     */
    void setBackTime();
}
