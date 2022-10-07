package com.ctgu.oamessage.service.core.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.constant.MessageConst;
import com.ctgu.oacommon.entity.Message;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.vo.MessageConditionVO;
import com.ctgu.oacommon.vo.MessageVO;
import com.ctgu.oacommon.vo.PageResult;
import com.ctgu.oacommon.vo.Result;
import com.ctgu.oamessage.mapper.MessageMapper;
import com.ctgu.oamessage.service.common.MessageServiceCommon;
import com.ctgu.oamessage.service.core.MessageServiceCore;
import com.ctgu.oauser.service.common.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 李嘉超
 * @version 1.0
 * @date 2022/8/16 16:22
 */
@Service
public class MessageServiceCoreImpl implements MessageServiceCore {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private MessageServiceCommon messageServiceCommon;
    @Autowired
    private UserCommonService userCommonService;

    @Override
    public Result<PageResult<Message>> getAllNotReleaseMessageBySendUserId(Long id, Integer current, Integer size) {
        //判断当前页和条数是不是不满足条件，不满足条件直接返回
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<>(listNull, 0);
            return Result.fail(userPageResult);
        }
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("su_id", id).eq("message_type", MessageConst.NOT_RELEASE);
        Page<Message> messagePage = messageMapper.selectPage(page, wrapper);
        List<Message> records = messagePage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Message message : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(message.getAuId());
                message.setAcceptUserName(acceptUserName);
                message.setSendUserName(sendUserName);
            }
            PageResult<Message> userPageResult = new PageResult<Message>(messagePage.getRecords(), (int) messagePage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.ok(userPageResult);
        }
    }

    @Override
    public Result<PageResult<Message>> getAllHaveReleasedMessageBySendUserId(Long id, Integer current, Integer size) {
        //判断当前页和条数是不是不满足条件，不满足条件直接返回
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.fail(userPageResult);
        }
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>();
        wrapper.eq("su_id", id).eq("message_type", MessageConst.HAVE_RELEASE);
        //List<Message> messages = messageMapper.selectList(wrapper);
        Page<Message> messagePage = messageMapper.selectPage(page, wrapper);
        List<Message> records = messagePage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Message message : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(message.getAuId());
                message.setAcceptUserName(acceptUserName);
                message.setSendUserName(sendUserName);
            }
            PageResult<Message> userPageResult = new PageResult<Message>(messagePage.getRecords(), (int) messagePage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.ok(userPageResult);
        }
    }

    @Override
    public Result<PageResult<Message>> getAllNotOverdueAndHaveReleasedMessageByAcceptUserId(Long id, Integer current, Integer size) {

        //先查询用户所有的已接受的消息，此时没考虑过期情况
        List<Message> allHaveReleasedMessage = messageServiceCommon.getAllHaveReleasedMessageByAcceptUserId(id);

        //下面就是一个用来判断当前请求时间与对应的过期时间对比，要是超过了就要设置消息为已过期的状态
        //这里我觉得应该使用一个动态添加过期执行的过期任务比较好，要是消息较多的话，for循环比较消耗性能
        for (Message value : allHaveReleasedMessage) {
            //首先过滤一下过期的消息，这些消息就不用在判断了，因为已经过期了
            //如果没有过期时间（null）就不用过期设置，一直保持不过期状态就可以了
            if (value.getMessageOverdue() == 0 && value.getMessageValidTime() != null) {
                //当前时间
                String dateStr1 = DateUtil.now();
                Date date1 = DateUtil.parse(dateStr1);
                //System.out.println("当前时间"+date1);
                //过期时间
                Date validTime = value.getMessageValidTime();
                String dateStr3 = DateUtil.format(validTime, "yyyy-MM-dd HH:mm:ss");
                Date date3 = DateUtil.parse(dateStr3);
                //System.out.println("过期时间"+date3);
                //比较是否过期，负数表示已经过期，正数表示在当前时间下还没有过期
                int timeCompareTo = date3.compareTo(date1);
                //System.out.println(timeCompareTo);
                //说明当前已经过期了，所以要更新消息过期标志message_overdue为1
                if (timeCompareTo < 0) {
                    //设置为过期状态
                    value.setMessageOverdue(MessageConst.HAVE_OVERDUE);
                    //System.out.println(allHaveReleasedMessage.get(i));
                    int update = messageMapper.updateById(value);
                    if (update == 0) {
                        throw new BizException("查询失败!");
                    }
                }
            }
        }
        //判断当前页和条数是不是不满足条件，不满足条件直接返回
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.fail(userPageResult);
        }
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>();
        wrapper//接收者
                .eq("au_id", id)
                //已发送
                .eq("message_type", MessageConst.HAVE_RELEASE)
                //没过期
                .eq("message_overdue", MessageConst.NOT_OVERDUE);
        Page<Message> messagePage = messageMapper.selectPage(page, wrapper);
        List<Message> records = messagePage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Message message : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(message.getAuId());
                message.setAcceptUserName(acceptUserName);
                message.setSendUserName(sendUserName);
            }
            PageResult<Message> userPageResult = new PageResult<Message>(messagePage.getRecords(), (int) messagePage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.ok(userPageResult);
        }
    }

    @Override
    public Result<Boolean> insertMessageNotRelease(MessageVO messageVO) {
        Message build = Message.builder()
                .suId(messageVO.getSuId())
                .auId(messageVO.getAuId())
                .messageHeader(messageVO.getMessageHeader())
                .messageContent(messageVO.getMessageContent())
                //设置为未发布，这个方法就是保存消息，不发送消息
                .messageType(0)
                .messageValidTime(DateUtil.parse(messageVO.getMessageValidTime()))
                .build();
        int insert = messageMapper.insert(build);

        if (insert == 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> insertMessageHaveRelease(MessageVO messageVO) {
        Message build = Message.builder()
                .suId(messageVO.getSuId())
                .auId(messageVO.getAuId())
                .messageHeader(messageVO.getMessageHeader())
                .messageContent(messageVO.getMessageContent())
                .messageTime(DateUtil.parse(DateUtil.now()))
                //设置消息未过期，只有发送消息之后才会有过期效果，未发送的消息没有过期效果
                .messageOverdue(MessageConst.NOT_OVERDUE)
                //设置为已发布，这个方法是发送消息
                .messageType(MessageConst.HAVE_RELEASE)
                .messageValidTime(DateUtil.parse(messageVO.getMessageValidTime()))
                .build();
        int insert = messageMapper.insert(build);

        if (insert == 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> updateMessageNotRelease(MessageVO messageVO) {
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>();
        wrapper.eq("id", messageVO.getId());
        //获取要修改的message对象
        Message message = messageMapper.selectOne(wrapper);
        //只有这几个属性需要修改
        message.setSuId(messageVO.getSuId());
        message.setMessageHeader(messageVO.getMessageHeader());
        message.setMessageContent(messageVO.getMessageContent());
        message.setMessageValidTime(DateUtil.parse(messageVO.getMessageValidTime()));
        QueryWrapper<Message> wrapperUpdate = new QueryWrapper<Message>();
        //这里表示只能修改未发送的消息，而不能修改已发送的消息
        wrapperUpdate.eq("id", messageVO.getId());
        wrapperUpdate.eq("message_type", 0);
        int update = messageMapper.update(message, wrapperUpdate);
        if (update == 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    @Override
    public Result<Boolean> deleteMessageById(Long id) {
        QueryWrapper<Message> wrapper = new QueryWrapper<Message>();
        wrapper.eq("id", id);
        int delete = messageMapper.delete(wrapper);
        if (delete == 0) {
            return Result.fail(false);
        }
        return Result.ok(true);
    }

    /**
     * 下面的就是消息的条件查询
     */
    //用户根据条件来查询所有自己已发送的消息
    @Override
    public Result<PageResult<Message>> getAllHaveReleasedMessageByCondition(Long currentUserId, MessageConditionVO messageConditionVO) {
        Integer current = messageConditionVO.getCurrent();
        Integer size = messageConditionVO.getSize();
        //判断当前页和条数是不是不满足条件，不满足条件直接返回
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.fail(userPageResult);
        }
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> wrapperAll = new QueryWrapper<Message>();
        String asUserName = messageConditionVO.getAsUserName();
        List<User> userIdByUserName = new ArrayList<>();
        if (!ObjectUtils.isEmpty(asUserName)) {
            userIdByUserName = userCommonService.getUserIdByUserName(asUserName);
        }
        //查询到了ID
        if (!ObjectUtils.isEmpty(userIdByUserName)) {
            //将找到的所有用户的id放在一个集合里面
            ArrayList<Long> longs = new ArrayList<>();
            for (User user : userIdByUserName) {
                longs.add(user.getId());
            }
            wrapperAll.in("au_id", longs);
        }
        //当前用户为发送者
        wrapperAll.eq("su_id", currentUserId);

        String beginTime = messageConditionVO.getBeginTime();
        Date beginDate = DateUtil.parse(beginTime);
        String endTime = messageConditionVO.getEndTime();
        Date endDate = DateUtil.parse(endTime);
        //如果同时有开始时间和结束时间的条件
        if (!ObjectUtils.isEmpty(beginTime) && !"".equals(beginTime) && !ObjectUtils.isEmpty(endTime) && !"".equals(endTime)) {
            wrapperAll.between("message_time", beginDate, endDate);
        }
        //如果只有开始时间，没有结束时间
        if (!ObjectUtils.isEmpty(beginTime) && !"".equals(beginTime)) {
            if (ObjectUtils.isEmpty(endTime) || "".equals(endTime)) {
                wrapperAll.between("message_time", beginDate, MessageConst.MAX_DATE);
            }
        }
        //如果只有结束时间，没有结开始时间
        if (!ObjectUtils.isEmpty(endTime) && !"".equals(endTime)) {
            if (ObjectUtils.isEmpty(beginTime) || "".equals(beginTime)) {
                wrapperAll.between("message_time", MessageConst.MIN_DATE, endDate);
            }
        }

        Page<Message> messagePage = messageMapper.selectPage(page, wrapperAll);
        List<Message> records = messagePage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Message message : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(message.getAuId());
                message.setAcceptUserName(acceptUserName);
                message.setSendUserName(sendUserName);
            }
            PageResult<Message> userPageResult = new PageResult<Message>(messagePage.getRecords(), (int) messagePage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.ok(userPageResult);
        }
    }

    //用户根据条件来查询所有自己未发送的消息
    @Override
    public Result<PageResult<Message>> getAllNotReleasedMessageByCondition(Long currentUserId, MessageConditionVO messageConditionVO) {
        Integer current = messageConditionVO.getCurrent();
        Integer size = messageConditionVO.getSize();
        //判断当前页和条数是不是不满足条件，不满足条件直接返回
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.fail(userPageResult);
        }
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> wrapperAll = new QueryWrapper<Message>();
        String asUserName = messageConditionVO.getAsUserName();
        List<User> userIdByUserName = new ArrayList<>();
        if (!ObjectUtils.isEmpty(asUserName)) {
            userIdByUserName = userCommonService.getUserIdByUserName(asUserName);
        }
        //查询到了ID
        if (!ObjectUtils.isEmpty(userIdByUserName)) {
            //将找到的所有用户的id放在一个集合里面
            ArrayList<Long> longs = new ArrayList<>();
            for (User user : userIdByUserName) {
                longs.add(user.getId());
            }
            wrapperAll.in("au_id", longs);
        }
        //当前用户为发送者
        wrapperAll.eq("su_id", currentUserId);
        //未发布
        wrapperAll.eq("message_type", MessageConst.NOT_RELEASE);
       /*
         因为未发送的消息没有时间约束，所以不用考虑开始时间和结束时间
        */
        Page<Message> messagePage = messageMapper.selectPage(page, wrapperAll);
        List<Message> records = messagePage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Message message : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(message.getAuId());
                message.setAcceptUserName(acceptUserName);
                message.setSendUserName(sendUserName);
            }
            PageResult<Message> userPageResult = new PageResult<Message>(messagePage.getRecords(), (int) messagePage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.ok(userPageResult);
        }
    }

    //用户获取根据条件获取所有自己收到的并且可以看到的消息（消息过期）
    @Override
    public Result<PageResult<Message>> getAllNotOverdueAndHaveReleasedMessage(Long currentUserId, MessageConditionVO messageConditionVO) {
        Integer current = messageConditionVO.getCurrent();
        Integer size = messageConditionVO.getSize();
        //判断当前页和条数是不是不满足条件，不满足条件直接返回
        if (current <= 0 || size <= 0 || ObjectUtils.isEmpty(current) || ObjectUtils.isEmpty(size)) {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<Message>(listNull, 0);
            return Result.fail(userPageResult);
        }
        Page<Message> page = new Page<>(current, size);
        QueryWrapper<Message> wrapperAll = new QueryWrapper<Message>();
        String asUserName = messageConditionVO.getAsUserName();
        List<User> userIdByUserName = new ArrayList<>();
        if (!ObjectUtils.isEmpty(asUserName)) {
            userIdByUserName = userCommonService.getUserIdByUserName(asUserName);
        }
        //查询到了ID
        if (!ObjectUtils.isEmpty(userIdByUserName)) {
            //将找到的所有用户的id放在一个集合里面
            ArrayList<Long> longs = new ArrayList<>();
            for (User user : userIdByUserName) {
                longs.add(user.getId());
            }
            wrapperAll.in("su_id", longs);
        }
        //当前用户为发送者
        wrapperAll.eq("au_id", currentUserId);
        /*
          因为接受的消息没有时间约束，所以不用考虑开始时间和结束时间
         */
        wrapperAll//已发送
                .eq("message_type", MessageConst.HAVE_RELEASE)
                //没过期
                .eq("message_overdue", MessageConst.NOT_OVERDUE);
        Page<Message> messagePage = messageMapper.selectPage(page, wrapperAll);
        List<Message> records = messagePage.getRecords();
        //获取发送者和接收者用户名
        if (!ObjectUtils.isEmpty(records)) {
            //发送者的获取一次就好了
            String sendUserName = userCommonService.getUserNameByUserId(records.get(0).getSuId());
            for (Message message : records) {
                //获取接收者用户名
                String acceptUserName = userCommonService.getUserNameByUserId(message.getAuId());
                message.setAcceptUserName(acceptUserName);
                message.setSendUserName(sendUserName);
            }
            PageResult<Message> userPageResult = new PageResult<>(messagePage.getRecords(), (int) messagePage.getTotal());

            return Result.ok(userPageResult);
        } else {
            List<Message> listNull = new ArrayList<>();
            //构造一个空的PageResult
            PageResult<Message> userPageResult = new PageResult<>(listNull, 0);
            return Result.ok(userPageResult);
        }
    }
}
