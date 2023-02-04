package com.ctgu.common.dao.checking;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.common.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface AttendanceMapper extends BaseMapper<Attendance> {
}
