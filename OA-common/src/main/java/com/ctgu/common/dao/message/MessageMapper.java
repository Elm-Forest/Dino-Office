package com.ctgu.common.dao.message;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.common.entity.Message;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息mapper
 *
 * @author Zhang Jinming
 * @create 11/8/2022 上午10:21
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
