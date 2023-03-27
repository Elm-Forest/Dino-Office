package com.ctgu.checking.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctgu.checking.service.core.CheckService;
import com.ctgu.common.dao.checking.*;
import com.ctgu.common.entity.*;
import com.ctgu.common.exception.BizException;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.utils.ThreadHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ctgu.common.constants.CommonConst.CHECK_IN;
import static com.ctgu.common.constants.CommonConst.CHECK_OUT;

/**
 * @author Li Zihan
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    private AttendanceMapper attendanceMapper;

    @Resource
    private AttendanceStatisticsMapper attendanceStatisticsMapper;

    @Resource
    private AttendanceRoleMapper attendanceRoleMapper;

    @Resource
    private IgnoreAttendanceTimeMapper iatMapper;

    @Resource
    private IgnoreAttendanceUserMapper iauMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> signIn() throws ParseException {
        AttendanceRole fattendanceRole = attendanceRoleMapper.selectById(1L);
        Date dateNow = new Date();
        User user = ThreadHolder.getCurrentUser();
        Long id = user.getId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date beginTime = fattendanceRole.getStartTime();
        Date endTime = fattendanceRole.getBackTime();
        Date nowTime;
        nowTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        if (getTime(beginTime, endTime)) {
            //添加考勤表
            Attendance attendance = Attendance.builder()
                    .attendanceTime(dateNow)
                    .userId(id)
                    .signIn(CHECK_IN)
                    .signBack(CHECK_OUT)
                    .build();
            //添加上下班表
            AttendanceRole attendanceRole = AttendanceRole.builder()
                    .userId(id)
                    .attendanceTime(dateNow)
                    .startTime(beginTime)
                    .backTime(endTime)
                    .beginTime(nowTime)
                    .build();
            int insert2 = attendanceRoleMapper.insert(attendanceRole);
            int insert = attendanceMapper.insert(attendance);
            if (insert == 0 || insert2 == 0) {
                throw new RuntimeException("签到失败");
            }
            return Result.ok();
        } else {
            throw new BizException("签到失败,请在规定时间段内签到");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> signOut() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        User user = ThreadHolder.getCurrentUser();
        Long id = user.getId();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间
        Date nowTime;
        // 当前时间
        nowTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        //更新打卡表
        LambdaUpdateWrapper<Attendance> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Attendance::getAttendanceTime, sdf.format(new Date()))
                .eq(Attendance::getUserId, id);
        Attendance attendance = new Attendance();
        attendance.setSignBack(CHECK_IN);
        int result = attendanceMapper.update(attendance, updateWrapper);
        //更新上下班表
        LambdaUpdateWrapper<AttendanceRole> update2Wrapper = new LambdaUpdateWrapper<>();
        update2Wrapper.eq(AttendanceRole::getAttendanceTime, sdf.format(new Date()))
                .eq(AttendanceRole::getUserId, id);
        AttendanceRole attendanceRole = new AttendanceRole();
        attendanceRole.setEndTime(nowTime);
        int result2 = attendanceRoleMapper.update(attendanceRole, update2Wrapper);
        if (result <= 0 || result2 <= 0) {
            throw new RuntimeException("签退失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findStatisticsById(Long userId) {
        LambdaUpdateWrapper<AttendanceStatistics> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttendanceStatistics::getUserId, userId);
        List<AttendanceStatistics> list = attendanceStatisticsMapper.selectList(updateWrapper);
        return Result.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setAttendanceRole(Date startTime, Date backTime) {
        LambdaUpdateWrapper<AttendanceRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttendanceRole::getId, 1L)
                .set(AttendanceRole::getStartTime, startTime)
                .set(AttendanceRole::getBackTime, backTime);
        AttendanceRole attendanceRole = new AttendanceRole();
        int result = attendanceRoleMapper.update(attendanceRole, updateWrapper);
        if (result <= 0) {
            throw new RuntimeException("更改上下班时间失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findStatistics() {
        List<AttendanceStatistics> list = attendanceStatisticsMapper.selectList(null);
        return Result.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setHoliday(Date beginTime, Date endTime) {
        LambdaUpdateWrapper<IgnoreAttendanceTime> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(IgnoreAttendanceTime::getId,1L)
                 .set(IgnoreAttendanceTime::getBeginTime, beginTime)
                .set(IgnoreAttendanceTime::getEndTime, endTime);
        IgnoreAttendanceTime iat = new IgnoreAttendanceTime();
        int result = iatMapper.update(iat, updateWrapper);
        if (result <= 0) {
            throw new RuntimeException("更改假期时间失败");
        }
        return Result.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setHolidayById(Long userId) {
        IgnoreAttendanceTime iat = iatMapper.selectById(1L);
        LambdaUpdateWrapper<IgnoreAttendanceUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(IgnoreAttendanceUser::getUserId, userId)
                .set(IgnoreAttendanceUser::getBeginTime, iat.getBeginTime())
                .set(IgnoreAttendanceUser::getEndTime, iat.getEndTime());
        IgnoreAttendanceUser iau = new IgnoreAttendanceUser();
        int result = iauMapper.update(iau, updateWrapper);
        if (result <= 0) {
            throw new RuntimeException("授予用户假期时间失败");
        }
        return Result.ok();
    }

    @Override
    public void setStartTime() {
        Date dateNow = new Date();
        LambdaUpdateWrapper<AttendanceRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttendanceRole::getId, 1L)
                .set(AttendanceRole::getStartTime, dateNow);
        AttendanceRole attendanceRole = new AttendanceRole();
        attendanceRoleMapper.update(attendanceRole, updateWrapper);
    }

    @Override
    public void setBackTime() {
        Date dateNow = new Date();
        LambdaUpdateWrapper<AttendanceRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttendanceRole::getId, 1L)
                .set(AttendanceRole::getBackTime, dateNow);
        AttendanceRole attendanceRole = new AttendanceRole();
        attendanceRoleMapper.update(attendanceRole, updateWrapper);
    }

    /**
     * 签到检查
     */
    public static Boolean getTime(Date beginTime, Date endTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime;
        try {
            nowTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException("签到失败，系统异常");
        }
        return checkTime(beginTime, endTime, nowTime);
    }

    /**
     * 检查时间
     */
    public static Boolean checkTime(Date beginTime, Date endTime, Date nowTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        return date.after(begin) && date.before(end);
    }
}
