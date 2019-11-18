package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Section;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SectionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Section record);



    Section selectByPrimaryKey(Long id);

   List<Section> selectByPageId(Long pageId);

    int updateByPrimaryKey(Section record);
}