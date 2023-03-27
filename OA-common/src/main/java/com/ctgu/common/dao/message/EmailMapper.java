package com.ctgu.common.dao.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.common.entity.Email;
import org.apache.ibatis.annotations.Mapper;

/**
 * 邮件mapper
 *
 * @author Zhang Jinming
 * @date 20/8/2022 下午5:55
 */
@Mapper
public interface EmailMapper extends BaseMapper<Email> {
}
