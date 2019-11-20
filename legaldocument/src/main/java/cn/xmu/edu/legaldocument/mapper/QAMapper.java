package cn.xmu.edu.legaldocument.mapper;

import cn.xmu.edu.legaldocument.vo.QA;
import cn.xmu.edu.legaldocument.entity.QASection;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface QAMapper {

    List<QA> getAllQA();

    //TODO mapper文件待完善
    void insertQASection(List<QASection> qaSectionList);
}