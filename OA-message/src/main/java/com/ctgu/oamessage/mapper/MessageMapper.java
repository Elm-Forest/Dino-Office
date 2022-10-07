package com.ctgu.oamessage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.oacommon.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Zhang Jinming
 * @create 11/8/2022 上午10:21
 */
@Repository//代表持久层
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
