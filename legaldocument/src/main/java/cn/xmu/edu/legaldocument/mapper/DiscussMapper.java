package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.Discuss;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DiscussMapper {
    String TABLE_NAME = " discuss ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addDiscuss(Discuss discuss);

    List<Discuss> selectLatestDiscuss(@Param("userId") int userId, @Param("offset") int offset,
                                      @Param("limit") int limit);

    Discuss getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

    Discuss getAddDiscuss(@Param("userId") int userId,@Param("offset") int offset,@Param("limit") int limit);
}

