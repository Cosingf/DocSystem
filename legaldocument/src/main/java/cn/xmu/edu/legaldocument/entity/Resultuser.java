package cn.xmu.edu.legaldocument.entity;

public class Resultuser {
    private Long id;

    private Long userId;

    private Byte likes;

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

    public Byte getLikes() {
        return likes;
    }

    public void setLikes(Byte likes) {
        this.likes = likes;
    }
}