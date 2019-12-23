package cn.xmu.edu.legaldocument.mapper;


import cn.xmu.edu.legaldocument.entity.WikiAnnotation;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface WikiMapper {
    WikiAnnotation getWikiByMatchingKeywords(String keyword);

    void insertWikiList(List<WikiAnnotation> list);
}