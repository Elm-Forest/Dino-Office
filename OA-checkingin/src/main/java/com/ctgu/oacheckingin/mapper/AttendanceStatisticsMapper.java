package com.ctgu.oacheckingin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.oacommon.entity.AttendanceStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface AttendanceStatisticsMapper extends BaseMapper<AttendanceStatistics> {
}
