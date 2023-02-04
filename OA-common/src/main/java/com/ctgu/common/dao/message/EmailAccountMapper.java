package com.ctgu.common.dao.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.common.entity.EmailAccount;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Zhang Jinming
 * @date 19/8/2022 下午5:32
 */
@Mapper
public interface EmailAccountMapper extends BaseMapper<EmailAccount> {
}
