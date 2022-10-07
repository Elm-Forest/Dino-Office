package com.ctgu.oamessage.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctgu.oacommon.constant.MessageConst;
import com.ctgu.oacommon.entity.Message;
import com.ctgu.oamessage.mapper.MessageMapper;
import com.ctgu.oamessage.service.common.MessageServiceCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 19:21
 */
@Service
public class MessageServiceCommonImpl implements MessageServiceCommon {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public List<Message> getAllHaveReleasedMessageByAcceptUserId(Long id) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("au_id",id)
                .eq("message_type", MessageConst.HAVE_RELEASE);
        return messageMapper.selectList(wrapper);
    }

    @Override
    public List<Message> getAllNotReleaseMessageByAcceptUserId(Long id) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("au_id",id).eq("message_type", MessageConst.NOT_RELEASE);
        return messageMapper.selectList(wrapper);
    }
}
