package com.ctgu.document.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.dao.document.DocumentLogMapper;
import com.ctgu.common.dao.user.UserInfoMapper;
import com.ctgu.common.entity.UserInfo;
import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.DocLogConditionVO;
import com.ctgu.common.models.vo.DocLogVO;
import com.ctgu.common.models.dto.PageResult;
import com.ctgu.common.utils.ThreadHolder;
import com.ctgu.document.service.DocumentLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
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
        Page<DocLogVO> page = new Page<>(currentPage, size);
        documentLogMapper.selectDocumentLog(page, deptId,
                docLogConditionVO.getName(),
                docLogConditionVO.getDocumentName(),
                docLogConditionVO.getOperation(),
                docLogConditionVO.getBeginTime(),
                docLogConditionVO.getEndTime()).getRecords();
        PageResult<?> docLog = new PageResult<>(page.getRecords(), (int) page.getTotal());
        return Result.ok(docLog);
    }
}
