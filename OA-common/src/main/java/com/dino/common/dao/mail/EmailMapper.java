package com.dino.common.dao.mail;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Email;
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
