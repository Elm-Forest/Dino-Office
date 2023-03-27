SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `id` int NOT NULL COMMENT '主键',
  `attendance_time` date NOT NULL COMMENT '考勤时间',
  `user_Id` bigint NOT NULL COMMENT '考勤用户编号',
  `sign_in` int NULL DEFAULT 0 COMMENT '是否签到(1-已签到，0-未签到)',
  `sign_back` int NULL DEFAULT 0 COMMENT '是否签退(1-已签退，0-未签退)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出勤表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attendance_role
-- ----------------------------
DROP TABLE IF EXISTS `attendance_role`;
CREATE TABLE `attendance_role`  (
  `id` int NOT NULL COMMENT '主键',
  `start_time` datetime NOT NULL COMMENT '规定上班时间',
  `back_time` datetime NOT NULL COMMENT '规定下班时间',
  `begin_time` datetime NOT NULL COMMENT '签到实际时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '签退实际时间',
  `user_id` bigint NOT NULL,
  `attendance_time` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_b`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_b` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出勤规则表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for attendance_statistics
-- ----------------------------
DROP TABLE IF EXISTS `attendance_statistics`;
CREATE TABLE `attendance_statistics`  (
  `id` int NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `attendance` double NULL DEFAULT NULL COMMENT '出勤率',
  `late_count` int NULL DEFAULT NULL COMMENT '迟到次数',
  `early_count` int NULL DEFAULT NULL COMMENT '早退次数',
  `absenteeism_count` int NULL DEFAULT NULL COMMENT '旷工次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `userid_statistics`(`user_id`) USING BTREE,
  CONSTRAINT `userid_statistics` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出勤统计表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for department
-- ----------------------------
DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`  (
  `id` bigint NOT NULL COMMENT '部门id',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地址',
  `head_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门图标',
  `describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for document
-- ----------------------------
DROP TABLE IF EXISTS `document`;
CREATE TABLE `document`  (
  `id` bigint NOT NULL COMMENT '主键',
  `dept_id` bigint NOT NULL COMMENT '文件所属部门编号',
  `create_id` bigint NOT NULL COMMENT '创建者编号(用户编号)',
  `modify_id` bigint NULL DEFAULT NULL COMMENT '修改者编号(用户编号)',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '文件名称',
  `extension` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `size` bigint NOT NULL COMMENT '文件大小/Byte',
  `type` tinyint NOT NULL COMMENT '文件类型，1:文件,2:文件夹',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件的下载地址',
  `file_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件存储路径',
  `modify_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `createuserid`(`create_id`) USING BTREE,
  INDEX `modifyid`(`modify_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for document_log
-- ----------------------------
DROP TABLE IF EXISTS `document_log`;
CREATE TABLE `document_log`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '操作者',
  `dept_id` bigint NOT NULL COMMENT '文档所属部门',
  `document_id` bigint NOT NULL COMMENT '此次操作的文档编号',
  `operation` int NOT NULL COMMENT '1-添加，2-修改，3-删除，4-恢复，5-彻底删除',
  `operation_time` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 285 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for email
-- ----------------------------
DROP TABLE IF EXISTS `email`;
CREATE TABLE `email`  (
  `id` bigint NOT NULL COMMENT '主键',
  `su_id` bigint NOT NULL COMMENT '发送方用户编号',
  `au_id` bigint NOT NULL COMMENT '接受方用户编号',
  `email_title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件主题',
  `email_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮件内容',
  `attachment_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '附件的完整存储路径',
  `send_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `send_email_type` int NOT NULL COMMENT '对于发送者1-收件箱，发件箱，2-草稿箱，3-废件箱 4-用户彻底删除',
  `accept_email_type` int NOT NULL COMMENT '对于接收者1-收件箱，发件箱，2-草稿箱，3-废件箱',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `senduserid`(`su_id`) USING BTREE,
  INDEX `acceptuserid`(`au_id`) USING BTREE,
  CONSTRAINT `acceptuserid` FOREIGN KEY (`au_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `senduserid` FOREIGN KEY (`su_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '邮件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for email_account
-- ----------------------------
DROP TABLE IF EXISTS `email_account`;
CREATE TABLE `email_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id，关联user.id',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱授权码',
  `host` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮件服务器',
  `port` int NOT NULL DEFAULT 465 COMMENT '端口',
  `protocol` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '协议',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1624814457748316162 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '邮箱账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for friends
-- ----------------------------
DROP TABLE IF EXISTS `friends`;
CREATE TABLE `friends`  (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `friend_id` bigint NOT NULL COMMENT '用户联系人ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_e`(`user_id`) USING BTREE,
  INDEX `friend_id_s`(`friend_id`) USING BTREE,
  CONSTRAINT `friend_id_s` FOREIGN KEY (`friend_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `user_id_e` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ignore_attendance_time
-- ----------------------------
DROP TABLE IF EXISTS `ignore_attendance_time`;
CREATE TABLE `ignore_attendance_time`  (
  `id` int NOT NULL COMMENT '主键',
  `begin_time` date NOT NULL COMMENT '开始时间',
  `end_time` date NOT NULL COMMENT '结束时间(这段时间内不用签到签退)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出勤忽略时间表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ignore_attendance_user
-- ----------------------------
DROP TABLE IF EXISTS `ignore_attendance_user`;
CREATE TABLE `ignore_attendance_user`  (
  `id` int NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `begin_time` date NOT NULL COMMENT '开始时间',
  `end_time` date NOT NULL COMMENT '结束时间(在这段时间里面此用户不用考勤)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '出勤忽略人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `su_id` bigint NOT NULL COMMENT '发送用户编号',
  `au_id` bigint NOT NULL COMMENT '接受用户编号',
  `message_header` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息标题',
  `message_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息内容',
  `message_time` datetime NULL DEFAULT NULL COMMENT '发送时间',
  `message_type` int NOT NULL COMMENT '消息类型，1表示已发送，0表示未发送',
  `message_valid_time` datetime NULL DEFAULT NULL COMMENT '消息有效时间，没有的话就是默认没有有效时间，永久有效',
  `message_overdue` int NULL DEFAULT NULL COMMENT '是否过期,1表示过期，0表示没过期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `send_id`(`su_id`) USING BTREE,
  INDEX `accept_id`(`au_id`) USING BTREE,
  CONSTRAINT `accept_id` FOREIGN KEY (`au_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `send_id` FOREIGN KEY (`su_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for note
-- ----------------------------
DROP TABLE IF EXISTS `note`;
CREATE TABLE `note`  (
  `id` bigint NOT NULL COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `note_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '便签主题',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_a`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_a` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '便签' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for recycle
-- ----------------------------
DROP TABLE IF EXISTS `recycle`;
CREATE TABLE `recycle`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件id',
  `user_id` bigint NOT NULL COMMENT '删除者id',
  `delete_time` datetime NOT NULL COMMENT '删除时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '移除标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1625085362441334787 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回收站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int NOT NULL COMMENT '关联user.role字段',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色名称',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色与部门' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_arrange
-- ----------------------------
DROP TABLE IF EXISTS `schedule_arrange`;
CREATE TABLE `schedule_arrange`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `schedule_title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日程标题',
  `schedule_content` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日程内容',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_s`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1631588377 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '个人日程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for schedule_department
-- ----------------------------
DROP TABLE IF EXISTS `schedule_department`;
CREATE TABLE `schedule_department`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `schedule_title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日程标题',
  `schedule_content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日程信息',
  `create_time` date NOT NULL COMMENT '创建时间',
  `begin_time` date NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` date NULL DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `dept_id_s`(`dept_id`) USING BTREE,
  CONSTRAINT `dept_id_s` FOREIGN KEY (`dept_id`) REFERENCES `department` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '企业日程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL COMMENT '主键，实体的主键必须是bigint类型，由雪花算法生成',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码，由MD5加密',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '盐值，用来确认密码',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证邮箱，用来找回密码',
  `role` tinyint NULL DEFAULT NULL COMMENT '职位角色，关联role表',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '激活状态',
  `rights` tinyint NULL DEFAULT NULL COMMENT '权限',
  `reg_time` datetime NULL DEFAULT NULL COMMENT '注册时间',
  `last_time` datetime NULL DEFAULT NULL COMMENT '上一次登录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint NOT NULL COMMENT '主键，关联user.id',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '公司id',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` tinyint NULL DEFAULT NULL COMMENT '性别[{女:0}{男:1}]',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所在地',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `head_img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像链接',
  `status` tinyint NULL DEFAULT 0 COMMENT '入职状态;0:未入职;1:已入职;2:已解聘',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户详细信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Event structure for 更新上班时间
-- ----------------------------
DROP EVENT IF EXISTS `更新上班时间`;
delimiter ;;
CREATE EVENT `更新上班时间`
ON SCHEDULE
EVERY '1' DAY STARTS '2022-08-21 08:30:00'
DO begin
UPDATE `attendance_role` SET `start_time` = NOW() WHERE `id` = '1';
end
;;
delimiter ;

-- ----------------------------
-- Event structure for 更新下班时间
-- ----------------------------
DROP EVENT IF EXISTS `更新下班时间`;
delimiter ;;
CREATE EVENT `更新下班时间`
ON SCHEDULE
EVERY '1' DAY STARTS '2022-08-21 19:41:00'
DO begin
UPDATE `attendance_role` SET `back_time` = NOW() WHERE `id` = '1';
end
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
