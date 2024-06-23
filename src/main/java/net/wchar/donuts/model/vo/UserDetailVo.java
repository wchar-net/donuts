package net.wchar.donuts.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.po.SysUserPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * 查询用户详情
 * @author Elijah
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Schema(name = "查询用户详情", description = "查询用户详情")
public class UserDetailVo {
    @Schema(description = "用户id")
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
    private String userAvatarReal;


    //帐号状态 (1正常 0停用)
    @Schema(description = "帐号状态 (1正常 0停用)")
    private Integer userStatus;


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

    @Schema(description = "部门id")
    private Long userDeptId;

    @Schema(description = "用户密码(保存的时候用)")
    private String userPwd;

    @Schema(description = "部门名称")
    private String userDeptName;

    @Schema(description = "部门")
    private DeptBo dept;

    @JsonIgnore
    public static UserDetailVo po2vo(SysUserPo po) {
        return UserDetailVo.builder().userId(po.getUserId()).remark(po.getRemark()).updateBy(po.getUpdateBy()).updateTime(po.getUpdateTime())
                .createBy(po.getCreateBy()).createTime(po.getCreateTime()).lastLoginDate(po.getLastLoginDate())
                .lastLoginIp(po.getLastLoginIp()).lastPwdUpdateDate(po.getLastPwdUpdateDate())
                .userStatus(po.getUserStatus()).userAvatar(po.getUserAvatar()).userSex(po.getUserSex())
                .userPhone(po.getUserPhone()).userEmail(po.getUserEmail()).userType(po.getUserType()).userNick(po.getUserNick())
                .userName(po.getUserName()).userId(po.getUserId()).build();
    }
    @JsonIgnore
    public static SysUserPo vo2po(UserDetailVo vo) {
        return SysUserPo.builder().userId(vo.getUserId()).remark(vo.getRemark()).updateBy(vo.getUpdateBy()).updateTime(vo.getUpdateTime())
                .createBy(vo.getCreateBy()).createTime(vo.getCreateTime()).lastLoginDate(vo.getLastLoginDate())
                .lastLoginIp(vo.getLastLoginIp()).lastPwdUpdateDate(vo.getLastPwdUpdateDate())
                .userStatus(vo.getUserStatus()).userAvatar(vo.getUserAvatar()).userSex(vo.getUserSex())
                .userPhone(vo.getUserPhone()).userEmail(vo.getUserEmail()).userType(vo.getUserType()).userNick(vo.getUserNick())
                .userName(vo.getUserName()).userId(vo.getUserId()).userPwd(vo.getUserPwd()).build();
    }
}
