package com.ctgu.common.constants;

/**
 * @author Zhang Jinming
 * @date 17/8/2022 上午11:14
 */
public class FileConst {
    /**
     * 文件路径常量
     */
    public static final String USER_FILE_PATH = "user/";

    public static final String DEPARTMENT_FILE_PATH = "department/";

    public static final String FILE_MANAGER = "cloud/";

    /**
     * 文件类型常量
     */
    public static final int TYPE_FILE = 1;

    public static final int TYPE_FOLDER = 2;

    /**
     * 文件日志常量
     */
    public static final int OPERATION_INSERT = 1;
    public static final int OPERATION_SELECT = 6;
    public static final int OPERATION_MODIFY = 2;
    public static final int OPERATION_DELETE = 3;
    public static final int OPERATION_RESTORE = 4;
    public static final int OPERATION_COMP_DELETE = 5;
    /**
     * 文件状态
     * 1：存在
     * 0：已移除
     */
    public static final int FILE_STATUS_LIVE = 1;
    public static final int FILE_STATUS_DIED = 0;
}
