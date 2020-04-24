package cn.xmu.edu.legaldocument.VO;


import cn.xmu.edu.legaldocument.entity.Discuss;
import cn.xmu.edu.legaldocument.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDiscuss {
    private int id;
    private String title;
    private String content;
    private int userId;
    private String createdDate;
    private int commentCount;
    private String account;
    private String email;

    public UserDiscuss(){
    }

    public UserDiscuss(Discuss discuss, User user){
        this.id=discuss.getId();
        this.title=discuss.getTitle();
        this.content=discuss.getContent();
        this.userId=discuss.getUserId();
        //date format
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date=discuss.getCreatedDate();
        String formatDate=df.format(date);
        this.createdDate=formatDate;
        this.commentCount=discuss.getCommentCount();
        this.account=user.getAccount();
        this.email=user.getEmail();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
