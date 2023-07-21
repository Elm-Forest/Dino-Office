package com.dino.common.dao.document;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dino.common.entity.DocumentLog;
import com.dino.common.models.dto.DocLogDTO;
import org.springframework.data.repository.query.Param;

/**
 * 文档日志mapper
 *
 * @author Zhang Jinming
 * @date 18/8/2022 下午2:47
 */
public interface DocumentLogMapper extends BaseMapper<DocumentLog> {
    /**
     * 查询文档日志
     *
     * @param page      分页对象
     * @param deptId    部门id
     * @param uName     用户名
     * @param dName     文档名
     * @param operation 操作
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 文档日志
     */
    Page<DocLogDTO> selectDocumentLog(@Param("page") Page<DocLogDTO> page,
                                      @Param("deptId") Long deptId,
                                      @Param("uName") String uName,
                                      @Param("dName") String dName,
                                      @Param("operation") Integer operation,
                                      @Param("beginTime") String beginTime,
                                      @Param("endTime") String endTime);
}
