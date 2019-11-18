package cn.xmu.edu.legaldocument.entity;

public class PersonalOcrStack {
    private Long id;

    private Long userId;

    private Long ocrTextId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOcrTextId() {
        return ocrTextId;
    }

    public void setOcrTextId(Long ocrTextId) {
        this.ocrTextId = ocrTextId;
    }
}