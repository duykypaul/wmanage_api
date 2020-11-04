package com.duykypaul.wmanage_api.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentReq {
    private Long id;
    private String content;
    private Integer upVote;
    private Integer downVote;
    private Long userId;
    private Long postId;
}
