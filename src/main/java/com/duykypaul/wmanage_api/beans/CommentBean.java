package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentBean extends BaseBean<CommentBean> {
    private String content;
    private int upVote;
    private int downVote;

    @JsonManagedReference
    private PostBean post = new PostBean();

    private UserBean user = new UserBean();

    public CommentBean() {
    }
}
