package com.ctgu.user.service.core;

import com.ctgu.common.models.dto.Result;
import com.ctgu.common.models.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Zhang Jinming
 * @date 16/8/2022 下午3:33
 */
public interface UserInfoService {
    Result<?> selectUserInfo();

    Result<?> updateUserInfo(UserInfoVO user);

    Result<?> insertUserInfo(UserInfoVO user);

    Result<?> updateUserHeadImg(MultipartFile headImg);

    Result<?> selectUserHeadImg();

    Result<?> entryApply(Long deptId);

    Result<?> checkEntry();

    Result<?> selectBaseInfo();
}
