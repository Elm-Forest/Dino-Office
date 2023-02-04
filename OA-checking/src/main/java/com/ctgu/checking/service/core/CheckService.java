package com.ctgu.checking.service.core;

import com.ctgu.common.models.dto.Result;

import java.text.ParseException;
import java.util.Date;

/**
 * @author Li Zihan
 */
public interface CheckService {
    Result<?> signIn() throws ParseException;

    Result<?> signOut() throws ParseException;

    Result<?> findStatisticsById(Long userId);

    Result<?> setAttendanceRole(Date startTime, Date backTime);

    Result<?> findStatistics();

    Result<?> setHoliday(Date beginTime, Date endTime);

    Result<?> setHolidayById(Long userId);

    void setStartTime();

    void setBackTime();
}
