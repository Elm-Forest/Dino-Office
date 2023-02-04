package com.ctgu.common.models.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Li Zihan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "考勤")
public class AttendanceVO {
    /**
     * 主键
     */
    private Integer id;

    @ApiModelProperty(name = "attendanceTime", value = "考勤时间", dataType = "Date")
    private Date attendanceTime;

    /**
     * 考勤用户编号
     */
    @ApiModelProperty(name = "userId", value = "考勤用户编号", dataType = "Long")
    private Long userId;

    /**
     * 是否签到(1-已签到，0-未签到)
     */
    @ApiModelProperty(name = "signIn", value = "签到标识", dataType = "Integer")
    private Integer signIn;

    /**
     * 是否签退(1-已签退，0-未签退)
     */
    @ApiModelProperty(name = "signBack", value = "签退标识", dataType = "Integer")
    private Integer signBack;

}
