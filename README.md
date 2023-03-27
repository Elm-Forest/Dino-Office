# AutoOffice - OA办公自动化

> 这是一个学习项目，不适用于生产，适合初学者练习和大学生课设

前端仓库[Click Here](https://github.com/Elm-Forest/OA-vue)，开发ing，少部分尚未开发完成

### 项目介绍

<img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/introduce.png?raw=true" width="35%" alt="" style="display: inline-block">

### 技术栈

springboot + mybatis-plus + shiro + jwt + websocket + redis + oss + druid + mysql

### 构建注意

JDK >=17

```shell
git clone https://github.com/Elm-Forest/AutoOffice.git
cd ./AutoOffice/OA-web/src/main/resources
# 此处修改或创建springboot配置文件 #
```

### 系统架构

<div>
    <img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/core.png?raw=true" width="35%" alt="" style="display: inline-block">
	<img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/structure.png?raw=true" width="50%" alt="" style="display: inline-block">
</div>

### 需求文档

企业角色：

> 客服\
> 财务\
> 技术工程师\
> 库管\
> 运营监督\
> HR\
> CEO

##### 1）文档管理平台

> 文档管理\
> 回收站管理\
> 文件搜索管理\
> 日志管理

- 各部门在服务器有自己的共享文件夹，可以在文件夹下创建某个文档，可以查询、修改、删除文档
- 实现回收站管理，可还原、彻底删除文件
- 实现文件搜索功能
- 记录文档操作的日志

##### 2）消息管理平台

> 消息管理\
> 邮箱管理\
> 信息提示

- 用户可以对自己未发布的消息进行增删改查
- 可以指定消息接收者、消息的有效时间等
- 管理员可以查询所有已发布的消息
- 用户可以进行邮箱管理，主要有收件箱、发件箱、草稿箱、废件箱的管理
- 用户登录系统后，在首页显示新消息，可以点击链接直接查看

##### 3）考勤管理平台

> 签到/签退管理\
> 考勤历史查询\
> 考勤统计\
> 工作日管理\
> 工作时间管理

- 用户如果是当天第一次操作，只能签到
- 当天第一次以后操作，只可以签退
- 记录签卡备注、签卡时间
- 管理员可以通过平台按照时间段、部门、姓名等信息查询考勤信息
- 管理员可以通过平台统计一段时间内指定部门所有员工的迟到、早退、旷工次数等，可以导出Excel表格
- 管理员可以通过本平台设定当年的工作日，对周末、法定假日、公司休假等日期不做考勤
- 管理员可以通过平台设定某段日期内上班时间、下班时间；员工可以将出差时间等信息提交给领导审批，审批通过后管理员设定该员工在出差时间段内不做考勤
- 员工当天如果忘记签到，事后可以提交申请进行补签

##### 4）日程管理平台

> 个人日程管理\
> 部门日程管理\
> 便签管理

- 用户可以浏览和编辑自己的日程
- 用户可以管理联系人名单
- 用户可以浏览联系人的日程
- 用户可以浏览和编辑部门日程
- 用户可以使用迷你日历，迷你日历是一个以图形方式简要显示当前日期、星期、月份的日历，为用户提供当前日期总览
- 用户可以记录便签

##### 5）用户管理平台

> 个人账户管理\
> 个人信息管理

- 用户可以管理自己的账户信息包含更改密码和邮箱
- 用户可以管理自己的详细信息
- 用户可以查看自己在部门的数据

##### 6）企业管理平台

> 企业信息管理\
> 员工管理

- 此模块面向组织权限大于等于2以上的用户开发，即部门管理员以上
- 管理者可以查看部门或企业的员工信息
- 管理者仅可修改的员工的职位，权限，状态信息
- 管理者可裁员
- 管理者可接收offer并同意或拒绝offer

### 软件架构

#### 安全架构

<div>
	<img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/security.png?raw=true" width="80%" alt="" >
</div>

### 权限与角色

<div>
    <img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/rights.png?raw=true" width="40%" alt="" style="display: inline-block">
	<img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/roles.png?raw=true" width="30%" alt="" style="display: inline-block">
</div>

### 数据库设计

<div>
	<img src="https://github.com/Elm-Forest/AutoOffice/blob/master/.github/imgs/database.png?raw=true" width="73%" alt="" >
</div>



