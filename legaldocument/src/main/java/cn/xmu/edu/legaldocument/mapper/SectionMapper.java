package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Section;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SectionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Section record);


    List<Section> selectByPageIdAndHighLightAndNum(@Param("pageId") Long pageId,@Param("str1") String str1,@Param("str2") String str2,@Param("num") String num);

    Section selectByPrimaryKey(Long id);

   List<Section> selectByPageId(Long pageId);

    int updateByPrimaryKey(Section record);
}