package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Page record);


    Page selectByPrimaryKey(Long id);

    Page selectByBookIdAndPageNum(@Param("bookId") Long bookId,@Param("orderNum")  Integer orderNum);

    List<Page> selectByBookId(@Param("bookId") Long bookId);
    int updateByPrimaryKey(Page record);

    Integer getPageNumByPageId(Long pageId);
}