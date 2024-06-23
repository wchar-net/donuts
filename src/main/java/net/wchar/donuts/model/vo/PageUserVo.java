package net.wchar.donuts.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 查询用户列表条件
 * @author Elijah
 */
@Data
@Schema(name = "查询用户列表", description = "查询用户列表")
public class PageUserVo {

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "手机号")
    private String userPhone;

    //用户性别（0男 1女 2未知）
    @Schema(description = "用户性别（0男 1女 2未知）")
    private Integer userSex;

    //帐号状态（1正常 0停用）
    @Schema(description = "帐号状态（1正常 0停用）")
    private Integer userStatus;

    //最后登录时间(yyyy-MM-dd)
    @Schema(description = "最后登录时间(yyyy-MM-dd)")
    private String lastLoginDate;

    @Schema(description = "pageIndex")
    private Integer pageIndex;

    @Schema(description = "pageSize")
    private Integer pageSize;

}
