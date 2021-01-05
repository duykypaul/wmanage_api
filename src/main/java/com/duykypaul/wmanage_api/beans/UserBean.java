package com.duykypaul.wmanage_api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserBean extends BaseBean<UserBean> {
    @JsonIgnore
    MultipartFile fileImage;
    //@NotBlank
    @Size(min = 3, max = 20)
    private String username;
    //@NotBlank
    @Email
    @Size(max = 50)
    private String email;

    private Set<RoleBean> roles;

    private BranchBean branch;

    private String avatar;
    private String permission;
    private boolean isEnabled;
    //@NotBlank
    @Size(min = 6, max = 30)
    private String password;
}
