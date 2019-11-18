package cn.xmu.edu.legaldocument.dao;

import cn.xmu.edu.legaldocument.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


@Repository
//这个注解代表这是一个mybatis的操作数据库的类
public interface UserDao {
    // 根据username获得一个User类
    @Select("select * from user where account=#{username} limit 1")
    User getOneUser(String username);


    //插入一个User
    @Insert("insert into user (account,password,userID,age,sex,email) values (#{username},#{password},#{userID},#{age},#{sex},#{email})")
    boolean setOneUser(User user);

}
