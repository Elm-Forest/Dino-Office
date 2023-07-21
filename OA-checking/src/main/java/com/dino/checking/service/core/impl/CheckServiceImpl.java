package com.dino.checking.service.core.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dino.checking.service.core.CheckService;
import com.dino.common.dao.checking.ApplyMapper;
import com.dino.common.dao.checking.CheckMapper;
import com.dino.common.dao.checking.CheckRuleMapper;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.dao.user.UserMapper;
import com.dino.common.entity.*;
import com.dino.common.exception.BizException;
import com.dino.common.models.dto.ApplyDTO;
import com.dino.common.models.dto.CheckDTO;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.ApplyVO;
import com.dino.common.models.vo.CheckConditionVO;
import com.dino.common.models.vo.CheckRuleVO;
import com.dino.common.models.vo.CheckVO;
import com.dino.common.utils.CommonUtils;
import com.dino.common.utils.ThreadHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author Li Zihan
 */
@Service
public class CheckServiceImpl implements CheckService {

    @Resource
    private CheckMapper checkMapper;

    @Resource
    private CheckRuleMapper checkRuleMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ApplyMapper applyMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 员工查看考勤日志
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> getCheckListById() throws BizException {
        Long id = ThreadHolder.getCurrentUser().getId();
        //获取该用户在该部门下的考勤日志
        List<Check> checkList = checkMapper.selectList(new LambdaQueryWrapper<Check>()
                .eq(Check::getUserId, id));
        return Result.ok(checkList);
    }

    /**
     * 员工查看缺勤日志
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> getAbsent() throws BizException {
        Long id = ThreadHolder.getCurrentUser().getId();
        //获取该用户在该部门下的缺勤日志
        List<Check> checkList = checkMapper.selectList(new LambdaQueryWrapper<Check>()
                .eq(Check::getUserId, id)
                .eq(Check::getType, 0));
        return Result.ok(checkList);
    }

    /**
     * 管理员查看考勤日志
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> getCheckList() throws BizException {
        //获取当前管理员账户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id));

        //获取当前管理员部门下的员工
        List<UserInfo> userInfoList = userInfoMapper.selectList(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getDeptId, userInfo.getDeptId()));

        Map<Long, String> idToName = new HashMap<>();

        List<Long> userIdList = new ArrayList<>();
        for (UserInfo info : userInfoList) {
            userIdList.add(info.getId());
            idToName.put(info.getId(), info.getName());
        }

        List<Check> checkList = checkMapper.selectList(new LambdaQueryWrapper<Check>().in(Check::getUserId, userIdList));
        List<CheckDTO> checkDTOList = new ArrayList<>();
        for (Check check : checkList) {
            String name = idToName.get(check.getUserId());
            CheckDTO checkDTO = CheckDTO.builder()
                    .checkId(check.getId())
                    .userId(check.getUserId())
                    .name(name)
                    .time(check.getTime())
                    .type(check.getType())
                    .build();
            checkDTOList.add(checkDTO);
        }

        return Result.ok(checkDTOList);
    }


    /**
     * 签到/签退
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> sign(Integer signType) {
        //获取当前登陆用户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id));

        //获取对应部门的考勤规则列表
        List<CheckRule> ruleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>()
                .eq(CheckRule::getDepartmentId, userInfo.getDeptId()));

        if (ruleList == null || ruleList.isEmpty()) {
            throw new BizException("部门暂未设置考勤打卡");
        }

        // 获取今天的日期
        LocalDate today = LocalDate.now();
        //获取现在的具体时间
        LocalTime nowTime = LocalTime.now();

        //获取当前时间符合的考勤规则
        CheckRule nowRule = null;
        for (CheckRule checkRule : ruleList) {
            LocalTime startTime = LocalTime.ofInstant(checkRule.getStartTime().toInstant(), ZoneId.systemDefault());
            LocalTime endTime = LocalTime.ofInstant(checkRule.getEndTime().toInstant(), ZoneId.systemDefault());

            if (nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
                nowRule = checkRule;
            }
        }

        if (nowRule == null) {
            throw new BizException("未在考勤时间内");
        }

        //获取该用户在该部门下的考勤日志
        List<Check> checkList = checkMapper.selectList(new LambdaQueryWrapper<Check>()
                .eq(Check::getUserId, id)
                .eq(Check::getCheckRuleId, nowRule.getId()));

        //判断是否可以签退
        int checkNum = 0;

        if (checkList != null && !checkList.isEmpty()) {
            for (Check check : checkList) {
                LocalDate checkTime = LocalDate.ofInstant(check.getTime().toInstant(), ZoneId.systemDefault());
                if (checkTime.isEqual(today)) {
                    checkNum++;
                }
            }
        }

        Check check;
        //可以签到
        if (checkNum == 0) {
            if (signType == 1) {
                throw new BizException("还未签到");
            }
            check = Check.builder()
                    .userId(id)
                    .checkRuleId(nowRule.getId())
                    .type(1)
                    .time(DateUtil.date())
                    .build();
            checkMapper.insert(check);
            return Result.ok("签到成功");

        }

        //可以签退
        if (checkNum == 1) {
            if (signType == 0) {
                throw new BizException("已签到");
            }
            check = Check.builder()
                    .userId(id)
                    .checkRuleId(nowRule.getId())
                    .type(2)
                    .time(DateUtil.date())
                    .build();
            checkMapper.insert(check);
            return Result.ok("签退成功");
        }

        throw new BizException("该时间段内考勤结束");
    }


    /**
     * 员工申请补卡
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> apply(ApplyVO applyVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        String startTime = applyVO.getApplyStartTime();
        String endTime = applyVO.getApplyEndTime();

        Apply apply = Apply.builder()
                .applyStartTime(new DateTime(startTime))
                .applyEndTime(new DateTime(endTime))
                .applyDes(applyVO.getDes())
                .type(applyVO.getType())
                .userId(id).build();
        applyMapper.insert(apply);
        return Result.ok("申请成功");
    }

    /**
     * 判断签到/签退
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> judge() {
        //获取当前登陆用户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id));

        //获取对应部门的考勤规则列表
        List<CheckRule> ruleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>()
                .eq(CheckRule::getDepartmentId, userInfo.getDeptId()));

        //该部门未设置考勤
        if (ruleList == null || ruleList.isEmpty()) {
            return Result.ok(3);
        }

        // 获取今天的日期
        LocalDate today = LocalDate.now();
        //获取现在的具体时间
        LocalTime nowTime = LocalTime.now();

        //获取当前时间符合的考勤规则
        CheckRule nowRule = null;
        for (CheckRule checkRule : ruleList) {
            LocalTime startTime = LocalTime.ofInstant(checkRule.getStartTime().toInstant(), ZoneId.systemDefault());
            LocalTime endTime = LocalTime.ofInstant(checkRule.getEndTime().toInstant(), ZoneId.systemDefault());

            if (nowTime.isAfter(startTime) && nowTime.isBefore(endTime)) {
                nowRule = checkRule;
            }
        }

        //不在考勤时间段内
        if (nowRule == null) {
            return Result.ok(4);
        }

        //获取该用户在该部门下的考勤日志
        List<Check> checkList = checkMapper.selectList(new LambdaQueryWrapper<Check>()
                .eq(Check::getUserId, id)
                .eq(Check::getCheckRuleId, nowRule.getId()));

        //判断是否可以签退
        int checkNum = 0;
        if (checkList != null && !checkList.isEmpty()) {
            for (Check check : checkList) {
                LocalDate checkTime = LocalDate.ofInstant(check.getTime().toInstant(), ZoneId.systemDefault());
                if (checkTime.isEqual(today)) {
                    checkNum++;
                }
            }
        }

        if (checkNum == 0) {
            return Result.ok(0);
        } else if (checkNum == 1) {
            return Result.ok(1);
        } else {
            return Result.ok(2);
        }
    }

    /**
     * 缺勤
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void absent() {
        LocalDate today = LocalDate.now();
        List<UserInfo> userInfoList = userInfoMapper.selectList(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (UserInfo userInfo : userInfoList) {
            Long id = userInfo.getId();

            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, id));
            if (user == null || user.getRights() == null || user.getRights() != 1) {
                continue;
            }

            //获取对应部门的考勤规则列表
            List<CheckRule> ruleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>()
                    .eq(CheckRule::getDepartmentId, userInfo.getDeptId()));

            if (ruleList == null || ruleList.isEmpty()) {
                continue;
            }

            for (CheckRule checkRule : ruleList) {
                List<Check> check = checkMapper.selectList(new LambdaQueryWrapper<Check>()
                        .eq(Check::getUserId, id)
                        .eq(Check::getCheckRuleId, checkRule.getId())
                        .like(Check::getTime, today.format(formatter)));
                if (check == null || check.isEmpty()) {
                    LocalTime startTime = LocalTime.ofInstant(checkRule.getStartTime().toInstant(), ZoneId.systemDefault());
                    LocalDateTime now = LocalDateTime.of(today, startTime);

                    Check start = Check.builder()
                            .userId(id)
                            .type(0)
                            .checkRuleId(checkRule.getId())
                            .time(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                            .build();
                    checkMapper.insert(start);
                }
            }

        }
    }

    /**
     * 管理员修改考勤
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setCheck(CheckVO checkVO) {
        Check check = checkMapper.selectOne(new LambdaQueryWrapper<Check>().eq(Check::getId, checkVO.getId()));
        if (check == null) {
            return Result.fail("修改不存在的考勤记录");
        }
        check.setType(checkVO.getType());
        checkMapper.updateById(check);
        return Result.ok("修改成功");
    }

    /**
     * 管理员删除考勤
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteCheck(Long id) {
        Check check = checkMapper.selectOne(new LambdaQueryWrapper<Check>().eq(Check::getId, id));
        if (check == null) {
            return Result.fail("删除不存在的考勤记录");
        }
        checkMapper.deleteById(id);
        return Result.ok("删除成功");
    }

    /**
     * 管理员根据条件查询考勤记录
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> selectCheck(CheckConditionVO checkConditionVO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String userName = checkConditionVO.getUserName();
        String startTime = checkConditionVO.getStartTime();
        String endTime = checkConditionVO.getEndTime();
        LocalDate start = null;
        LocalDate end = null;

        int index;
        if (!CommonUtils.strIsNullOrEmpty(userName, startTime, endTime)) {
            index = 1;
        } else if (!CommonUtils.strIsNullOrEmpty(userName)) {
            index = 2;
        } else if (!CommonUtils.strIsNullOrEmpty(startTime, endTime)) {
            index = 3;
        } else {
            return Result.ok();
        }

        if (index != 2) {
            start = LocalDate.parse(startTime, formatter);
            end = LocalDate.parse(endTime, formatter);
        }

        //获取当前管理员账户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id));

        //获取当前管理员部门下的员工
        List<UserInfo> userInfoList = userInfoMapper.selectList(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getDeptId, userInfo.getDeptId()));

        Map<Long, String> idToName = new HashMap<>();

        List<Long> userIdList = new ArrayList<>();
        for (UserInfo info : userInfoList) {
            userIdList.add(info.getId());
            idToName.put(info.getId(), info.getName());
        }

        List<Check> checkList = checkMapper.selectList(new LambdaQueryWrapper<Check>().in(Check::getUserId, userIdList));
        List<CheckDTO> checkDTOList = new ArrayList<>();


        for (Check check : checkList) {
            String name = idToName.get(check.getUserId());
            if (index != 3 && !name.contains(userName)) {
                continue;
            }
            if (index != 2) {
                LocalDate time = LocalDate.ofInstant(check.getTime().toInstant(), ZoneId.systemDefault());
                if (!((time.isAfter(start) || time.isEqual(start)) && (time.isBefore(end)) || time.isEqual(end))) {
                    continue;
                }
            }

            CheckDTO checkDTO = CheckDTO.builder()
                    .checkId(check.getId())
                    .userId(check.getUserId())
                    .name(name)
                    .time(check.getTime())
                    .type(check.getType())
                    .build();
            checkDTOList.add(checkDTO);
        }

        return Result.ok(checkDTOList);
    }


    /**
     * 管理员查看考勤规则
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findRule() throws BizException {
        //获取当前管理员账户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id)).getDeptId();
        List<CheckRule> checkRuleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>().eq(CheckRule::getDepartmentId, deptId));
        return Result.ok(checkRuleList);
    }

    /**
     * 管理员添加考勤规则
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> addRule(CheckRuleVO checkRuleVO) {
        String startTime = checkRuleVO.getStartTime();
        String endTime = checkRuleVO.getEndTime();

        //获取当前管理员账户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id)).getDeptId();
        List<CheckRule> checkRuleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>()
                .eq(CheckRule::getDepartmentId, deptId));

        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        for (CheckRule checkRule : checkRuleList) {
            LocalTime localStart = checkRule.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime localEnd = checkRule.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

            if (!(end.isBefore(localStart) || start.isAfter(localEnd))) {
                return Result.fail("添加考勤规则失败, 与当前考勤规则冲突");
            }
        }

        CheckRule checkRule = CheckRule.builder()
                .startTime(new DateTime(startTime))
                .endTime(new DateTime(endTime))
                .departmentId(deptId).build();
        checkRuleMapper.insert(checkRule);
        return Result.ok("添加考勤规则成功");
    }

    /**
     * 管理员删除考勤规则
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> delRule(Long id) {
        int del = checkRuleMapper.delete(new LambdaQueryWrapper<CheckRule>()
                .eq(CheckRule::getId, id));
        if (del == 0) {
            throw new RuntimeException("删除考勤规则失败");
        }
        return Result.ok("删除考勤规则成功");
    }

    /**
     * 管理员修改考勤规则
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> setRule(CheckRuleVO checkRuleVO) {
        CheckRule checkRule = checkRuleMapper.selectOne(new LambdaQueryWrapper<CheckRule>()
                .eq(CheckRule::getId, checkRuleVO.getId()));

        if (checkRule == null) {
            return Result.fail("修改不存在的考勤规则");
        }

        String startTime = checkRuleVO.getStartTime();
        String endTime = checkRuleVO.getEndTime();
        //获取当前管理员账户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id)).getDeptId();
        List<CheckRule> checkRuleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>()
                .eq(CheckRule::getDepartmentId, deptId));

        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        for (CheckRule nowRule : checkRuleList) {
            if (nowRule == checkRule || nowRule.equals(checkRule)) {
                continue;
            }
            LocalTime localStart = nowRule.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            LocalTime localEnd = nowRule.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if (!(end.isBefore(localStart) || start.isAfter(localEnd))) {
                return Result.fail("修改考勤规则失败, 与当前考勤规则冲突");
            }
        }

        CheckRule checkRuleResult = CheckRule.builder()
                .id(checkRule.getId())
                .departmentId(checkRule.getDepartmentId())
                .startTime(new DateTime(startTime))
                .endTime(new DateTime(endTime))
                .build();
        checkRuleMapper.updateById(checkRuleResult);
        return Result.ok();
    }

    /**
     * 管理员查看补卡申请
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> findApply() throws BizException {
        //获取当前管理员账户信息
        Long id = ThreadHolder.getCurrentUser().getId();
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getId, id));

        //获取当前管理员部门下的员工
        List<UserInfo> userInfoList = userInfoMapper.selectList(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getDeptId, userInfo.getDeptId()));

        Map<Long, String> idToName = new HashMap<>();

        List<Long> userIdList = new ArrayList<>();
        for (UserInfo info : userInfoList) {
            userIdList.add(info.getId());
            idToName.put(info.getId(), info.getName());
        }

        List<Apply> applyList = applyMapper.selectList(new LambdaQueryWrapper<Apply>().in(Apply::getUserId, userIdList));
        List<ApplyDTO> applyDTOList = new ArrayList<>();
        for (Apply apply : applyList) {
            String name = idToName.get(apply.getUserId());
            ApplyDTO applyDTO = ApplyDTO.builder()
                    .id(apply.getId())
                    .userId(apply.getUserId())
                    .name(name)
                    .applyStartTime(apply.getApplyStartTime())
                    .applyEndTime(apply.getApplyEndTime())
                    .type(apply.getType())
                    .applyDes(apply.getApplyDes())
                    .build();
            applyDTOList.add(applyDTO);
        }

        return Result.ok(applyDTOList);
    }

    /**
     * 管理员拒绝补卡申请
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteApply(Long id) throws BizException {
        Apply apply = applyMapper.selectOne(new LambdaQueryWrapper<Apply>().eq(Apply::getId, id));

        if (apply == null) {
            return Result.fail("删除不存在的申请");
        }

        applyMapper.deleteById(apply);
        return Result.ok("删除成功");
    }

    /**
     * 管理员同意补卡申请
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> approveApply(Long id) {
        Apply apply = applyMapper.selectOne(new LambdaQueryWrapper<Apply>().eq(Apply::getId, id));
        if (apply == null) {
            return Result.fail("同意不存在的申请");
        }
        Integer type = apply.getType() == 0 ? 1 : 3;
        Long userId = apply.getUserId();
        LocalDate applyStartDate = apply.getApplyStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate applyEndDate = apply.getApplyEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        applyEndDate = applyEndDate.plusDays(1);
        if (type == 1) {
            List<Check> userCheckList = checkMapper.selectList(new LambdaQueryWrapper<Check>()
                    .eq(Check::getUserId, userId)
                    .between(Check::getTime, applyStartDate, applyEndDate));
            for (Check check : userCheckList) {
                if (check.getType() == 0) {
                    check.setType(type);
                    checkMapper.updateById(check);
                }
            }
        }
        if (type == 3) {
            Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                    .eq(UserInfo::getId, userId)).getDeptId();
            List<CheckRule> checkRuleList = checkRuleMapper.selectList(new LambdaQueryWrapper<CheckRule>()
                    .eq(CheckRule::getDepartmentId, deptId));
            LocalDate currentDate = applyStartDate;
            while (!currentDate.isEqual(applyEndDate)) {
                for (CheckRule nowRule : checkRuleList) {
                    LocalTime localStart = nowRule.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
                    LocalDateTime start = LocalDateTime.of(currentDate, localStart);

                    Check check = Check.builder()
                            .userId(userId)
                            .type(type)
                            .checkRuleId(nowRule.getId())
                            .time(Date.from(start.atZone(ZoneId.systemDefault()).toInstant()))
                            .build();
                    checkMapper.insert(check);
                }
                currentDate = currentDate.plusDays(1);
            }
        }

        applyMapper.deleteById(apply);
        return Result.ok("申请通过");
    }
}
