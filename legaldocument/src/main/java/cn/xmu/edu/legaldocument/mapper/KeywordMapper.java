package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.VO.KeywordWikiVO;
import cn.xmu.edu.legaldocument.entity.Keyword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface KeywordMapper {

    void insertKeywordList(List<Keyword> keywordList);

    Long getLastId();

    List<Keyword> getKeywordByBookId(@Param("bookId") Long bookId);
}