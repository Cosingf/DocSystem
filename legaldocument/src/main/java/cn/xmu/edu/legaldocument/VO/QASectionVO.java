package cn.xmu.edu.legaldocument.VO;

import cn.xmu.edu.legaldocument.entity.QA;

import java.util.List;

public class QASectionVO {
    private Long sectionId;
    private Integer sectionNum;
    private List<QA> qas;

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Integer getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(Integer sectionNum) {
        this.sectionNum = sectionNum;
    }

    public List<QA> getQas() {
        return qas;
    }

    public void setQas(List<QA> qas) {
        this.qas = qas;
    }
}
