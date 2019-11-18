package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Selection;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SelectionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Selection record);

    int insertSelective(Selection record);

    Selection selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Selection record);

    int updateByPrimaryKey(Selection record);
}