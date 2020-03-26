package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.PersonalLegaldocStack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonalLegaldocStackMapper {
    int deleteByPrimaryKey(Long id);

    int deleteByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);

    int insert(PersonalLegaldocStack record);

    PersonalLegaldocStack selectByPrimaryKey(Long id);

    List<PersonalLegaldocStack> selectPageByUserId(@Param("userId") Long userId,@Param("num1") Integer num1,@Param("num2") Integer num2);

    List<PersonalLegaldocStack> selectByUserId(@Param("userId") Long userId);

    int getUserAllNumByUserId(@Param("userId") Long userId);

    int updateByPrimaryKey(PersonalLegaldocStack record);

    PersonalLegaldocStack selectByBookIdAndUserId(@Param("bookId") Long bookId, @Param("userId") Long userId);
}
