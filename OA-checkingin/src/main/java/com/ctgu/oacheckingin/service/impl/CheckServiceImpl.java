package com.ctgu.oacheckingin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ctgu.oacheckingin.mapper.*;
import com.ctgu.oacommon.entity.*;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oacheckingin.service.CheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.ctgu.oacommon.constant.CommonConst.CHECK_IN;
import static com.ctgu.oacommon.constant.CommonConst.CHECK_OUT;

/**
 * @author Li Zihan
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;

    @Autowired
    private AttendanceRoleMapper attendanceRoleMapper;

    @Autowired
    private IgnoreAttendanceTimeMapper iatMapper;

    @Autowired
    private IgnoreAttendanceUserMapper iauMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> signIn() throws ParseException {
        AttendanceRole fattendanceRole = attendanceRoleMapper.selectById(1L);
        Calendar ca = Calendar.getInstance();
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //设置时间为当前时间
        ca.setTime(new Date());
        ca.add(Calendar.DATE, -1);
        // 获取当前时间
        Date dateNow = new Date();
        //获取前一天时间
        ca.getTime();
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        // SimpleDateFormat指定日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间
        Date beginTime = fattendanceRole.getStartTime();
        Date endTime = fattendanceRole.getBackTime();
        Date nowTime;
        // 当前时间
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
            System.out.println(attendanceRole);
            int insert2 = attendanceRoleMapper.insert(attendanceRole);
            System.out.println(attendance);
            int insert = attendanceMapper.insert(attendance);
            if (insert == 0 || insert2 == 0) {
                return Result.fail("未知原因，签到失败");
            }
            return Result.ok("签到成功");
        } else {
            return Result.fail("签到失败,请在规定时间段内签到");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> signOut() throws ParseException {
        Calendar ca = Calendar.getInstance();
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //设置时间为当前时间
        ca.setTime(new Date());
        ca.add(Calendar.DATE, -1);
        // 获取当前时间
        User user = UserThreadHolder.getCurrentUser();
        Long id = user.getId();
        // SimpleDateFormat指定日期格式
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
            return Result.fail("签退失败");
        }
        return Result.ok("签退成功");
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
            return Result.fail("更改上下班时间失败");
        }
        return Result.ok("更改上下班时间成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findStatistics() {
        List<AttendanceStatistics> list = attendanceStatisticsMapper.selectList(null);
        return Result.ok(list);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setHoilday(Date beginTime, Date endTime) {
        LambdaUpdateWrapper<IgnoreAttendanceTime> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(IgnoreAttendanceTime::getBeginTime, beginTime)
                .set(IgnoreAttendanceTime::getEndTime, endTime);
        IgnoreAttendanceTime iat = new IgnoreAttendanceTime();
        int result = iatMapper.update(iat, updateWrapper);
        if (result <= 0) {
            return Result.fail("更改假期时间失败");
        }
        return Result.ok("更改假期时间成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setHoildayById(Long userId) {
        IgnoreAttendanceTime iat = iatMapper.selectById(1L);
        LambdaUpdateWrapper<IgnoreAttendanceUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(IgnoreAttendanceUser::getUserId, userId)
                .set(IgnoreAttendanceUser::getBeginTime, iat.getBeginTime())
                .set(IgnoreAttendanceUser::getEndTime, iat.getEndTime());
        IgnoreAttendanceUser iau = new IgnoreAttendanceUser();
        int result = iauMapper.update(iau, updateWrapper);
        if (result <= 0) {
            return Result.fail("授予用户假期时间失败");
        }
        return Result.ok("授予用户假期时间成功");
    }

    @Override
    public void setStartTime() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        Date dateNow = new Date();// 获取当前时间
        LambdaUpdateWrapper<AttendanceRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttendanceRole::getId, 1L)
                .set(AttendanceRole::getStartTime, dateNow);
        AttendanceRole attendanceRole = new AttendanceRole();
        attendanceRoleMapper.update(attendanceRole, updateWrapper);
    }

    @Override
    public void setBackTime() {
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -1);
        Date dateNow = new Date();// 获取当前时间
        LambdaUpdateWrapper<AttendanceRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AttendanceRole::getId, 1L)
                .set(AttendanceRole::getBackTime, dateNow);
        AttendanceRole attendanceRole = new AttendanceRole();
        attendanceRoleMapper.update(attendanceRole, updateWrapper);
    }

    //签到检查
    public static Boolean getTime(Date s, Date b) {
        // SimpleDateFormat指定日期格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间
        Date beginTime = null;
        Date endTime = null;
        Date nowTime = null;
        try {
            // 开始时间
            beginTime = s;
            // 结束时间
            endTime = b;
            // 当前时间
            nowTime = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return checkTime(beginTime, endTime, nowTime);
    }

    //检查时间
    public static Boolean checkTime(Date beginTime, Date endTime, Date nowTime) {
        //设置当前时间
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);
        //设置开始时间
        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);
        //设置结束时间
        Calendar end = Calendar.getInstance();
        end.setTime(endTime);
        //处于开始时间之后，和结束时间之前的判断(不包含开始和结束时间）
        return date.after(begin) && date.before(end);
    }
}
