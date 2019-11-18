package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PageMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Page record);


    Page selectByPrimaryKey(Long id);

    Page selectByBookIdAndPageNum(@Param("bookId") Long bookId,@Param("orderNum")  Integer orderNum);

    int updateByPrimaryKey(Page record);
}