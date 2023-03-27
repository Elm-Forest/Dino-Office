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
 * 文档mapper
 *
 * @author Zhang Jinming
 * @create 11/8/2022 上午10:22
 */
@Repository
public interface DocumentMapper extends BaseMapper<Document> {
    /**
     * 查询文档
     *
     * @param deptId   部门id
     * @param filePath 文件路径
     * @return 文档列表
     */
    List<DocumentDTO> selectDocument(Long deptId, String filePath);

    /**
     * 根据条件查询文档
     *
     * @param page      分页信息
     * @param deptId    部门id
     * @param filePath  文件路径
     * @param uName     用户名
     * @param dName     部门名
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 文档列表
     */
    Page<DocumentDTO> selectDocumentCondition(@Param("page") Page<DocumentDTO> page,
                                              @Param("deptId") Long deptId,
                                              @Param("filePath") String filePath,
                                              @Param("uName") String uName,
                                              @Param("dName") String dName,
                                              @Param("beginTime") String beginTime,
                                              @Param("endTime") String endTime);

    /**
     * 查询文档id
     *
     * @param deptId   部门id
     * @param filePath 文件路径
     * @return 文档id列表
     */
    @Select("select `document`.id " +
            "from document " +
            "where document.dept_id = #{deptId} " +
            "and document.file_path = #{filePath}")
    List<Long> selectDocumentId(Long deptId, String filePath);

    /**
     * 查询可用文档id
     *
     * @param deptId   部门id
     * @param filePath 文件路径
     * @return 文档id列表
     */
    @Select("SELECT DISTINCT document.id " +
            "FROM document " +
            "LEFT JOIN recycle ON document.id = recycle.id " +
            "WHERE recycle.delete_time IS NULL " +
            "AND document.dept_id = #{deptId} " +
            "AND document.file_path = #{filePath}")
    List<Long> selectAvailableDocumentsId(Long deptId, String filePath);

    /**
     * 查询过期文档id
     *
     * @param filePath 文件路径
     * @return 文档id列表
     */
    @Select("select `document`.id " +
            "from document " +
            "where document.file_path = #{filePath}")
    List<Long> selectOverTimeDocumentsId(String filePath);
}
