package com.dino.document.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dino.common.dao.document.DocumentLogMapper;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.entity.UserInfo;
import com.dino.common.models.dto.DocLogDTO;
import com.dino.common.models.dto.PageResult;
import com.dino.common.models.dto.Result;
import com.dino.common.models.vo.DocLogConditionVO;
import com.dino.common.utils.CommonUtils;
import com.dino.common.utils.ThreadHolder;
import com.dino.document.service.DocumentLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 */
@Service
public class DocumentLogServiceImpl implements DocumentLogService {
    @Resource
    private DocumentLogMapper documentLogMapper;
    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public Result<?> selectDocumentLog(DocLogConditionVO docLogConditionVO) {
        Long id = ThreadHolder.getCurrentUser().getId();
        Long deptId = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getDeptId).eq(UserInfo::getId, id)).getDeptId();
        int currentPage = docLogConditionVO.getCurrent();
        int size = docLogConditionVO.getSize();
        String endTime = docLogConditionVO.getEndTime();
        endTime = CommonUtils.dateMoveOneDay(endTime);
        Page<DocLogDTO> page = new Page<>(currentPage, size);
        documentLogMapper.selectDocumentLog(page, deptId,
                docLogConditionVO.getName(),
                docLogConditionVO.getDocumentName(),
                docLogConditionVO.getOperation(),
                docLogConditionVO.getBeginTime(),
                endTime).getRecords();
        PageResult<?> docLog = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(docLog);
    }
}
