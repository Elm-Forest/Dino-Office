package com.ctgu.oacheckingin.service;

import com.ctgu.oacommon.vo.Result;

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

    Result<?> setHoilday(Date beginTime, Date endTime);

    Result<?> setHoildayById(Long userId);

    void setStartTime();

    void setBackTime();
}
