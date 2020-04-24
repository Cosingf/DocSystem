package cn.xmu.edu.legaldocument.VO;


import cn.xmu.edu.legaldocument.entity.Comment;
import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserComment {
    private int id;
    private int userId;
    private int entityId;
    private int entityType;
    private String content;
    private String createdDate;
    private int status;
    private String account;
    private long likeCount;
    private long likeStatus;

    public UserComment(){
    }

    public UserComment(Comment comment, User user,long likeCount,long likeStatus){
        this.id=comment.getId();
        this.userId=comment.getUserId();
        this.entityId=comment.getEntityId();
        this.entityType=comment.getEntityType();
        this.content=comment.getContent();
        //date format
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date=comment.getCreatedDate();
        String formatDate=df.format(date);
        this.createdDate=formatDate;
        this.status=comment.getStatus();
        this.account=user.getAccount();
        //点赞数
        this.likeCount=likeCount;
        this.likeStatus=likeStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public long getLikeStatus() {
        return likeStatus;
    }

    public void setLikeStatus(long likeStatus) {
        this.likeStatus = likeStatus;
    }
}
