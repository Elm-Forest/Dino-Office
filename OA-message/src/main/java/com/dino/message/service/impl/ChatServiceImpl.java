package com.dino.message.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.dino.common.constants.RedisPrefixConst;
import com.dino.common.dao.message.ChatMapper;
import com.dino.common.dao.user.UserInfoMapper;
import com.dino.common.entity.Chat;
import com.dino.common.entity.UserInfo;
import com.dino.common.models.dto.ChatDTO;
import com.dino.common.models.dto.NotificationDTO;
import com.dino.common.models.dto.Result;
import com.dino.common.utils.Assert;
import com.dino.common.utils.ThreadHolder;
import com.dino.message.service.ChatService;
import com.dino.redis.service.RedisService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Zhang Jinming
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private ChatMapper chatMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisService redisService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMessage(Long connectorId, Long myId, String content) {
        int flag = chatMapper.insert(Chat.builder()
                .auId(connectorId)
                .suId(myId)
                .content(content)
                .time(DateUtil.date())
                .build());
        Assert.greaterThanZero(flag, new RuntimeException("保存失败"));
    }

    @Override
    public Result<?> selectMessage(Long connectorId) {
        Long id = ThreadHolder.getCurrentUser().getId();
        List<ChatDTO> chatDTO = chatMapper.selectChats(id, connectorId);
        for (ChatDTO chat : chatDTO) {
            chat.setIsSelf(chat.getSuId().equals(id));
        }
        return Result.ok(chatDTO);
    }

    @Override
    public Result<?> selectConnectorInfo(Long id) {
        UserInfo userInfo = userInfoMapper.selectById(id);
        Assert.objNotNull(userInfo, new RuntimeException("用户不存在"));
        return Result.ok(userInfo);
    }

    @Override
    public Result<?> selectConnectorLists() {
        List<UserInfo> userInfos = userInfoMapper.selectConnectors(ThreadHolder.getCurrentUser().getId());
        return Result.ok(userInfos);
    }

    @Override
    public Result<?> saveNotification(Long suId, Long auId, String message) {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .id(IdUtil.simpleUUID())
                .auId(auId)
                .suId(suId)
                .suName(userInfoMapper.selectById(suId).getName())
                .content(message)
                .build();
        redisService.lPush(RedisPrefixConst.NOTIFICATION + auId, notificationDTO);
        return Result.ok(notificationDTO);
    }

    @Override
    public Result<?> selectNotification() {
        Long auId = ThreadHolder.getCurrentUser().getId();
        List<Object> notificationLists = redisService.lRange(RedisPrefixConst.NOTIFICATION + auId, 0, -1);
        return Result.ok(notificationLists);
    }

    @Override
    public Result<?> deleteNotification(String id) {
        Long auId = ThreadHolder.getCurrentUser().getId();
        List<Object> objectLists = redisService.lRange(RedisPrefixConst.NOTIFICATION + auId, 0, -1);
        for (Object obj : objectLists) {
            NotificationDTO obj1 = (NotificationDTO) obj;
            if (obj1.getId().equals(id)) {
                redisService.lRemoveNotification(RedisPrefixConst.NOTIFICATION + auId, 1, obj1);
            }
        }
        return Result.ok();
    }
}
