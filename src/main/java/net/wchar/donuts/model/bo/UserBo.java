package net.wchar.donuts.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户信息
 * @author Elijah
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Schema(name = "用户信息", description = "用户信息")
public class UserBo {

    @Schema(name = "用户id", description = "用户id")
    private Long userId;


    @Schema(description = "用户名称")
    private String userName;


    @Schema(description = "用户昵称")
    private String userNick;

    //用户来源(0内部 1外部) 默认0
    @Schema(description = "用户来源(0内部 1外部) 默认0")
    private Integer userType;

    @Schema(description = "用户邮箱")
    private String userEmail;


    @Schema(description = "用户联系电话")
    private String userPhone;


    //用户性别（0男 1女 2未知）
    @Schema(description = "用户性别（0男 1女 2未知）")
    private Integer userSex;

    //头像
    @Schema(description = "用户头像")
    private String userAvatar;


    @Schema(description = "用户头像地址(上传后,后端返回的)")
    private String userPwd;

    //帐号状态 (1正常 0停用)
    @Schema(description = "帐号状态 (1正常 0停用)")
    private Integer userStatus;

    //1正常 0删除
    @Schema(description = "1正常 0删除")
    private Integer delFlag;


    @Schema(description = "用户部门名称")
    private String deptName;

    @Schema(description = "最后登录ip")
    private String lastLoginIp;

    @Schema(description = "最后登录日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginDate;

    //密码最后更新时间
    @Schema(description = "密码最后更新时间")
    private LocalDateTime lastPwdUpdateDate;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "修改人")
    private String updateBy;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;
}
