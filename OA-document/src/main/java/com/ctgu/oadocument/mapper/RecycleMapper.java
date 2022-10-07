package com.ctgu.oadocument.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ctgu.oacommon.entity.Recycle;
import com.ctgu.oacommon.vo.RecycleVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:43
 */
@Mapper
public interface RecycleMapper extends BaseMapper<Recycle> {
    List<RecycleVO> selectRecDoc(Long deptId);

    List<Long> selectTimeOutIds();
}
