package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    User selectByAccount(String account);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}