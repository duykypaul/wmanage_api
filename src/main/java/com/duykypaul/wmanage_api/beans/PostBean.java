package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PostBean extends BaseBean<PostBean> {

    private String content;
    private String urlImage;
    private int status;
    private int count;

    @JsonIgnore
    MultipartFile fileImage;

    private UserBean user = new UserBean();
    private Set<CategoryBean> categories;

    @JsonBackReference
    private List<CommentBean> comments;

    private String lstCategoryReq;

    public PostBean() {
    }
}
