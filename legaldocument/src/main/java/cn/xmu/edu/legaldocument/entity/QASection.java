package cn.xmu.edu.legaldocument.entity;

public class QASection {
    private QA qa;

    public QASection(QA qa, Section section) {
        this.qa = qa;
        this.section = section;
    }

    public QASection() {
    }

    public QA getQa() {
        return qa;
    }

    public void setQa(QA qa) {
        this.qa = qa;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    private Section section;
}
