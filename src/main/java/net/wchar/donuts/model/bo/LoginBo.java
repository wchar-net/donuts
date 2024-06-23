package net.wchar.donuts.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户bo
 * @author Elijah
 */
@Schema(name = "用户bo", description = "用户bo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class LoginBo {

    @Schema(description = "xToken")
    private String xToken;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户头像")
    public String userAvatar;


    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户昵称")
    private String userNick;

    //部门信息
    @Schema(description = "部门信息")
    private List<DeptBo> deptList;

    //权限字符串
    @Schema(description = "权限字符串")
    private  List<String> perms;

    //角色信息
    @Schema(description = "角色信息")
    private List<RoleBo> roles;
}
