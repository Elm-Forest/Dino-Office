package com.ctgu.common.dao.document;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ctgu.common.entity.Document;
import com.ctgu.common.models.dto.DocumentDTO;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Zhang Jinming
 * @create 11/8/2022 上午10:22
 */
@Repository
public interface DocumentMapper extends BaseMapper<Document> {
    List<DocumentDTO> selectDocument(Long deptId, String filePath);

    Page<DocumentDTO> selectDocumentCondition(@Param("page") Page<DocumentDTO> page,
                                              @Param("deptId") Long deptId,
                                              @Param("uName") String uName,
                                              @Param("dName") String dName,
                                              @Param("beginTime") String beginTime,
                                              @Param("endTime") String endTime);

    @Select("select `document`.id " +
            "from document " +
            "where document.dept_id=#{deptId} and document.file_path like concat(#{filePath},'%')")
    List<Long> selectDocumentId(Long deptId, String filePath);
}
