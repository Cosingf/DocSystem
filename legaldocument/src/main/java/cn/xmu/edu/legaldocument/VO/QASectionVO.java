package cn.xmu.edu.legaldocument.VO;

import cn.xmu.edu.legaldocument.entity.QA;

import java.util.List;

public class QASectionVO {
    private Long sectionId;
    private Integer sectionNum;
    private String sectionContent;
    private String question;
    private String answer;
    private String link;
    private Long questionId;
    private Long answerId;

    public  QASectionVO (QA qa,Long sectionId,Integer sectionNum,String sectionContent){
        this.sectionId=sectionId;
        this.sectionNum=sectionNum;
        this.sectionContent =sectionContent;
        question = qa.getQuestion();
        answer =qa.getAnswer();
        link = qa.getLink();
        questionId =qa.getQuestionId();
        answerId = qa.getAnswerId();
    }

    public String getSectionContent() {
        return sectionContent;
    }

    public void setSectionContent(String sectionContent) {
        this.sectionContent = sectionContent;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

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

}
