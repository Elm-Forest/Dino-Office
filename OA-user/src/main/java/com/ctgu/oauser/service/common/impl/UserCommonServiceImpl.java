package com.ctgu.oauser.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ctgu.oacommon.constant.RedisPrefixConst;
import com.ctgu.oacommon.dto.EmailDTO;
import com.ctgu.oacommon.entity.Department;
import com.ctgu.oacommon.entity.User;
import com.ctgu.oacommon.entity.UserInfo;
import com.ctgu.oacommon.exception.BizException;
import com.ctgu.oacommon.service.mailService.MailService;
import com.ctgu.oacommon.utils.CommonUtils;
import com.ctgu.oacommon.vo.UserInfoVO;
import com.ctgu.oacommon.vo.UserVO;
import com.ctgu.oaredis.service.RedisService;
import com.ctgu.oauser.mapper.UserDepartmentMapper;
import com.ctgu.oauser.mapper.UserInfoMapper;
import com.ctgu.oauser.mapper.UserMapper;
import com.ctgu.oauser.service.common.UserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

import static com.ctgu.oacommon.constant.CommonConst.ACTIVATED;
import static com.ctgu.oacommon.constant.RedisPrefixConst.CODE_EXPIRE_TIME;
import static java.util.Objects.nonNull;

/**
 * @author Zhang Jinming
 * @date 15/8/2022 下午9:27
 */
@Service
public class UserCommonServiceImpl implements UserCommonService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserDepartmentMapper departmentMapper;

    @Autowired
    private MailService mailService;

    @Override
    public User selectUserByUserName(String username) {
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void checkActivated(Integer status) {
        if (status != ACTIVATED) {
            throw new BizException("账号已失效或未激活");
        }
    }

    @Override
    public Boolean checkUser(UserVO userVO) {
        System.out.println(userVO);
        if (!userVO.getCode().equals(redisService.get(RedisPrefixConst.USER_CODE_KEY + userVO.getEmail()))) {
            throw new BizException("验证码错误！");
        }
        User email = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getEmail, userVO.getEmail()));
        User username = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, userVO.getUsername()));
        return (nonNull(email) || nonNull(username));
    }

    @Override
    public Boolean checkEmail(String email) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getEmail, email));
        return nonNull(user);
    }

    @Override
    public int updateUserInfoById(UserInfoVO user) {
        return userInfoMapper.updateById(UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .sex(user.getSex())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .build());
    }

    @Override
    public int insertUserInfoById(UserInfoVO user) {
        return userInfoMapper.insert(UserInfo.builder()
                .id(user.getId())
                .name(user.getName())
                .deptId(user.getDeptId())
                .sex(user.getSex())
                .phone(user.getPhone())
                .email(user.getEmail())
                .address(user.getAddress())
                .build());
    }

    @Override
    public List<Department> selectDepartmentByName(String name) {
        return departmentMapper.selectList(new LambdaQueryWrapper<Department>()
                .like(Department::getName, name));
    }

    @Override
    public void sendCode(String email, String msg) {
        if (!CommonUtils.checkEmail(email)) {
            throw new BizException("请输入正确邮箱");
        }
        String code = CommonUtils.getRandomCode();
        System.out.println("已生成验证码：" + code);
        int deadline = (int) CODE_EXPIRE_TIME / 60;
        System.out.println("已设置验证码的过期时间为：" + deadline + "min");
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject("【AutoOffice】开发者测试邮件")
                .content("您正在进行" + msg + "操作<br>" +
                        "您的验证码为 " + code + " 有效期" + deadline + "分钟，请不要告诉他人哦！<br>" +
                        "如果您不清楚这封邮件的来源，非常抱歉我们误发了邮件<br>" +
                        "我们正在进行项目测试，如果您反复收到此邮件，烦请通过此邮箱与我们联系")
                .build();
        try {
            System.out.println("邮件线程正在启动");
            mailService.sendMail(emailDTO);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        redisService.set(RedisPrefixConst.USER_CODE_KEY + email, code, CODE_EXPIRE_TIME);
    }

    @Override
    public List<User> getUserIdByUserName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.like("username", username);
        return userMapper.selectList(wrapper);
    }

    @Override
    public String getUserNameByUserId(Long id) {
        return userMapper.getUserNameByUserId(id);
    }


}
