package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.entity.QA;
import cn.xmu.edu.legaldocument.entity.QASection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
public interface QAMapper {

    List<QA> getAllQA();

    void insertQASection(@Param("list") List<QASection> list);

//    Long getLastQASectionId();

}