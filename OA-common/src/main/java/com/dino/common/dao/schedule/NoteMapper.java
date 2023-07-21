package com.dino.common.dao.schedule;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dino.common.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author Li Zihan
 */
@Mapper
@Repository
public interface NoteMapper extends BaseMapper<Note> {
}
