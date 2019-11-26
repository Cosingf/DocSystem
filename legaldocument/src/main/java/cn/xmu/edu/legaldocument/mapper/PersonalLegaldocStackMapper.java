package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.PersonalLegaldocStack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonalLegaldocStackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PersonalLegaldocStack record);

    PersonalLegaldocStack selectByPrimaryKey(Long id);

    List<PersonalLegaldocStack> selectPageByUserId(@Param("userId") Long userId,@Param("num") Integer num);

    List<PersonalLegaldocStack> selectByUserId(@Param("userId") Long userId);

    int updateByPrimaryKey(PersonalLegaldocStack record);

    PersonalLegaldocStack selectByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);
}