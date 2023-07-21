package com.dino.checking.service.core;

import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.ApplyVO;
import com.dino.common.models.vo.CheckConditionVO;
import com.dino.common.models.vo.CheckRuleVO;
import com.dino.common.models.vo.CheckVO;

import java.text.ParseException;

/**
 * @author Li Zihan
 */
public interface CheckService {

    /**
     * 员工查看考勤日志表
     *
     * @return Result
     */
    Result<?> getCheckListById();

    /**
     * 管理员查看考勤日志
     *
     * @return Result
     */
    Result<?> getCheckList();

    /**
     * 员工查看缺勤
     *
     * @return Result
     */
    Result<?> getAbsent();

    /**
     * 员工签到/签退
     *
     * @param signType 签到类型
     * @return Result
     * @throws ParseException parseException
     */
    Result<?> sign(Integer signType) throws ParseException;

    /**
     * 判断签到/签退
     *
     * @return Result
     * @throws ParseException parseException
     */
    Result<?> judge() throws ParseException;

    /**
     * 员工申请补卡
     *
     * @param applyVO 补卡信息
     * @return Result
     * @throws ParseException parseException
     */
    Result<?> apply(ApplyVO applyVO) throws ParseException;

    /**
     * 缺勤
     *
     * @throws ParseException parseException
     */
    void absent() throws ParseException;

    /**
     * 管理员修改考勤
     *
     * @param checkVO 考勤信息
     * @throws ParseException parseException
     */
    Result<?> setCheck(CheckVO checkVO) throws ParseException;

    /**
     * 管理员删除考勤
     *
     * @param id 考勤id
     * @throws ParseException parseException
     */
    Result<?> deleteCheck(Long id) throws ParseException;

    /**
     * 管理员根据条件查询考勤记录
     *
     * @param checkConditionVO 查询条件
     * @return Result
     */
    Result<?> selectCheck(CheckConditionVO checkConditionVO) throws ParseException;

    /**
     * 管理员添加考勤规则
     *
     * @param checkRuleVO 考勤规则
     * @return Result
     */
    Result<?> addRule(CheckRuleVO checkRuleVO) throws ParseException;

    /**
     * 管理员删除考勤规则
     *
     * @param id 考勤规则id
     * @return Result
     */
    Result<?> delRule(Long id);

    /**
     * 管理员修改考勤规则
     *
     * @param checkRuleVO 考勤规则
     * @return Result
     */
    Result<?> setRule(CheckRuleVO checkRuleVO);

    /**
     * 管理员查看考勤规则
     *
     * @return Result
     */
    Result<?> findRule();

    /**
     * 管理员查看补卡申请
     *
     * @return Result
     */
    Result<?> findApply();

    /**
     * 管理员拒绝补卡申请
     *
     * @param id 补卡申请id
     * @return Result
     */
    Result<?> deleteApply(Long id);

    /**
     * 管理员同意补卡申请
     *
     * @param id 补卡申请id
     * @return Result
     */
    Result<?> approveApply(Long id);
}
