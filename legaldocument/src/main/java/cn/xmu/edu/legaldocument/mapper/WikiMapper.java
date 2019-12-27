package cn.xmu.edu.legaldocument.mapper;


import cn.xmu.edu.legaldocument.entity.WikiAnnotation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;

@Mapper
public interface WikiMapper {
    WikiAnnotation getWikiByMatchingKeywords(String keyword);

    void insertWikiList(List<WikiAnnotation> list);

    WikiAnnotation matchWikiCorpusByKeyword(@Param("keyword")String keyword);

    WikiAnnotation getWikiByWikiId(Long wikiId);
}