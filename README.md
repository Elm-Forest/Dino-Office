# Dino-Office - OA办公自动化平台

> 这是一个学习项目，不适用于生产，适合初学者参考或大学生毕设课设

前端仓库[Click Here](https://github.com/Elm-Forest/OA-vue)

### 项目介绍

<img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/introduce.png?raw=true" width="80%" alt="" style="display: inline-block">

### 技术栈

springboot + mybatis-plus + shiro + jwt + websocket + redis + oss + druid + mysql

### API文档

[Apifox](https://apifox.com/apidoc/shared-6431cbf6-65c9-4713-9464-71acdb12925a)

### CI/CD指南
[使用GitHub Action自动构建与部署项目](https://github.com/Elm-Forest/Dino-Office/wiki)

### 构建注意

JDK >= 17

```shell
git clone https://github.com/Elm-Forest/Dino-Office.git
cd ./Dino-Office/OA-web/src/main/resources
# 此处修改或创建springboot配置文件 #
```

### 系统架构

<div>
    <img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/core.png?raw=true" width="30%" alt="" style="display: inline-block">
	<img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/structure.png?raw=true" width="55%" alt="" style="display: inline-block">
</div>

### 需求文档

企业角色：

> 客服\
> 财务\
> 技术\
> 采购\
> 运营\
> HR\
> CEO

##### 1）企业云盘

> 文件管理\
> 回收站\
> 日志管理

- 企业成员可以在自己的企业云盘中进行文件相关操作
- 文件管理支持上传、下载、删除、重命名、新建文件夹、计算大小、展示归属、修改展示等功能
- 回收站管理，可还原、彻底删除文件
- 文档操作日志记录修改人、时间、部门、操作方式等信息

##### 2）企业邮件

> 发邮件\
> 发件箱\
> 收件箱\
> 草稿箱\
> 垃圾箱\
> 账户管理\

- 用户可以绑定自己第三方邮箱账户，可以在本平台收发邮件
- 邮件编辑器使用富文本编辑器，支持在线搜索联系人
- 发件箱、收件箱、草稿箱、垃圾箱支持以富文本形式展示邮件
- 草稿箱支持恢复邮件
- 发件箱、收件箱、草稿箱可以删除邮件，将移入垃圾箱，垃圾箱支持彻底删除邮件

##### 3）企业消息

> 站内私信\
> GPT助理

- 站内私信包含聊天栏与右上角小铃铛提示
- 站内私信模块UI参考微信聊天设计栏，基于socket即时通信
- GPT助理是一个经过prompt微调的AI助手，可以帮助用户解决工作问题，也可以进行闲聊
- GPT助理模块支持创建会话，加载会话，删除会话
- 加载会话后，支持恢复过去的聊天记录

##### 4）考勤与审批

> 员工签到/签退\
> 员工考勤查询\
> 员工申请审批\
> 部门考勤统计\
> 部门考勤规则管理\
> 部门审批管理

- 用户如果是当天第一次操作，只能签到
- 当天第一次以后操作，只可以签退
- 记录签卡备注、签卡时间
- 管理员可以通过平台按照时间段、部门、姓名等信息查询考勤信息
- 管理员可以通过平台统计一段时间内指定部门所有员工的迟到、早退、旷工次数等，可以导出Excel表格
- 管理员可以通过本平台设定当年的工作日，对周末、法定假日、公司休假等日期不做考勤
- 管理员可以通过平台设定某段日期内上班时间、下班时间；员工可以将出差时间等信息提交给领导审批，审批通过后管理员设定该员工在出差时间段内不做考勤
- 员工当天如果忘记签到，事后可以提交申请进行补签

##### 5）日程管理

> 个人日程管理\
> 部门日程管理

- 日程以迷你日历展示，迷你日历是一个以图形方式简要显示当前日期、星期、月份的日历，为用户提供当前日期总览
- 用户可以浏览和编辑自己的日程
- 员工可以浏览部门日程
- 管理员可以编辑部门日程

##### 6）个人管理

> 个人账户管理\
> 个人信息管理

- 用户可以管理自己的账户信息
- 用户可以管理自己的资料信息

##### 7）企业管理

> 企业信息管理\
> 员工管理

- 管理员可修改企业信息
- 管理员可修改的员工的职位，权限，状态信息
- 管理员查看员工的资料信息
- 管理员可以管理offer

### 软件架构

#### 安全架构

<div>
	<img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/security.png?raw=true" width="80%" alt="" >
</div>

### 权限与角色

<div>
    <img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/rights.png?raw=true" width="40%" alt="" style="display: inline-block">
	<img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/roles.png?raw=true" width="30%" alt="" style="display: inline-block">
</div>

### 数据库设计

<div>
	<img src="https://github.com/Elm-Forest/Dino-Office/blob/master/.github/imgs/database.png?raw=true" width="73%" alt="" >
</div>



