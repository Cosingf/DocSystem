package cn.xmu.edu.legaldocument.entity;

public class QA {
    String question;
    String answer;
    String link;

    public  QA(String que,String ans,String link){
        question=que;
        answer=ans;
        this.link=link;
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
}
