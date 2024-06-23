package net.wchar.donuts.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 用户
 * @author Elijah
 */
@TableName("sys_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class SysUserPo {
    @TableId(type = IdType.AUTO, value = "user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("user_nick")
    private String userNick;

    //用户来源(0内部 1外部) 默认0
    @TableField("user_type")
    private Integer userType;

    @TableField("user_email")
    private String userEmail;

    @TableField("user_phone")
    private String userPhone;


    //用户性别（0男 1女 2未知）
    @TableField("user_sex")
    private Integer userSex;

    //头像
    @TableField("user_avatar")
    private String userAvatar;

    @TableField("user_pwd")
    private String userPwd;

    //帐号状态 (1正常 0停用)
    @TableField("user_status")
    private Integer userStatus;

    //1正常 0删除
    @TableField("del_flag")
    private Integer delFlag;

    @TableField("last_login_ip")
    private String lastLoginIp;

    @TableField("last_login_date")
    private LocalDateTime lastLoginDate;

    //密码最后更新时间
    @TableField("last_pwd_update_date")
    private LocalDateTime lastPwdUpdateDate;

    @TableField("create_by")
    private String createBy;
    @TableField("update_by")
    private String updateBy;
    @TableField("create_time")
    private LocalDateTime createTime;
    @TableField("update_time")
    private LocalDateTime updateTime;
    @TableField("remark")
    private String remark;
}
