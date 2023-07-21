package com.dino.common.dao.document;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Recycle;
import com.dino.common.models.dto.RecycleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午4:43
 */
@Mapper
public interface RecycleMapper extends BaseMapper<Recycle> {
    /**
     * 查询回收站文档
     *
     * @param deptId 部门id
     * @return 回收站文档列表
     */
    List<RecycleDTO> selectRecDoc(Long deptId);

    /**
     * 查询过期的回收站文件夹
     *
     * @return 回收站文件夹列表
     */
    List<Long> selectTimeOutIds();
}
