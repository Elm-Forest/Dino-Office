package com.ctgu.oadocument.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.oacommon.entity.DocumentLog;
import com.ctgu.oacommon.vo.DocLogVO;
import org.springframework.data.repository.query.Param;

/**
 * @author Zhang Jinming
 * @date 18/8/2022 下午2:47
 */
public interface DocumentLogMapper extends BaseMapper<DocumentLog> {
    Page<DocLogVO> selectDocumentLog(@Param("page") Page<DocLogVO> page,
                                     @Param("deptId") Long deptId,
                                     @Param("uName") String uName,
                                     @Param("dName") String dName,
                                     @Param("operation") Integer operation,
                                     @Param("beginTime") String beginTime,
                                     @Param("endTime") String endTime);
}
