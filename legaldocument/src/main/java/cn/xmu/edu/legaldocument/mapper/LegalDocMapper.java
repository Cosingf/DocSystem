package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.LegalDoc;
import cn.xmu.edu.legaldocument.entity.PersonalLegaldocStack;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LegalDocMapper {
    int deleteByPrimaryKey(Long id);

    int insert(LegalDoc record);

    LegalDoc selectByPrimaryKey(Long id);

    List<LegalDoc> selectAll();

    List<LegalDoc> selectPublicPage(@Param("num") Integer num);

    List<LegalDoc>  selectPublicBooksByName(@Param("selectName") String selectName);

    int updateByPrimaryKey(LegalDoc record);

    Long getLastBookId();

    void insertPersonalLegalDoc(PersonalLegaldocStack personalLegaldocStack);

    void setLegalDocEnriched(Long id);
}