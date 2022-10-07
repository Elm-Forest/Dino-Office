package com.ctgu.oadocument.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.utils.UserThreadHolder;
import com.ctgu.oacommon.vo.DocLogConditionVO;
import com.ctgu.oacommon.vo.DocLogVO;
import com.ctgu.oacommon.vo.PageResult;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oadocument.mapper.DocumentLogMapper;
import com.ctgu.oadocument.service.DocumentLogService;
import com.ctgu.oauser.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:59
 */
@Service
public class DocumentLogServiceImpl implements DocumentLogService {
    @Autowired
    private DocumentLogMapper documentLogMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Result<?> selectDocumentLog(DocLogConditionVO docLogConditionVO) {
        System.out.println(docLogConditionVO);
        Long id = UserThreadHolder.getCurrentUser().getId();
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
