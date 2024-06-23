package net.wchar.donuts.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 登录
 * @author Elijah
 */
@TableName("sys_login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class SysLoginPo {

    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "x_token")
    private String xToken;


    @TableField(value = "login_time")
    private LocalDateTime loginTime;

    @TableField(value = "user_agent")
    private String userAgent;

    @TableField(value = "login_ip")
    private String loginIp;

    @TableField(value = "login_exp_time")
    private LocalDateTime loginExpTime;


    /**
     * 登录状态 1有效 0无效
     */
    @TableField(value = "login_status")
    private Integer loginStatus;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(value = "create_by")
    private String createBy;


    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 1正常 0删除
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

}
