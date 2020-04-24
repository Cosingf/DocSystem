package cn.xmu.edu.legaldocument.VO;

import java.util.List;

public class DiscussComment {
    UserDiscuss userDiscuss;
    List<UserComment> userCommentList;

    public DiscussComment(){
    }

    public DiscussComment(UserDiscuss userDiscuss, List<UserComment> userCommentList){
        this.userDiscuss=userDiscuss;
        this.userCommentList=userCommentList;
    }

}
