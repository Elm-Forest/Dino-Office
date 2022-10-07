package com.ctgu.oamessage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.oacommon.entity.Email;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Zhang Jinming
 * @date 20/8/2022 下午5:55
 */
@Repository//代表持久层
@Mapper
public interface EmailMapper extends BaseMapper<Email> {
}
