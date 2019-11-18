package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Resultuser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResultuserMapper {
    int insert(Resultuser record);

    int insertSelective(Resultuser record);
}