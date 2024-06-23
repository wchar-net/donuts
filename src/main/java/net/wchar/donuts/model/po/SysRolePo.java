package net.wchar.donuts.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 角色
 * @author Elijah
 */
@Data
@Builder
@ToString
@TableName("sys_role")
public class SysRolePo {

    @TableId(type = IdType.AUTO, value = "role_id")
    private Long roleId;

    @TableField(value = "role_name")
    private String roleName;

    //1正常 0禁用
    @TableField(value = "status")
    private Integer status;


    //1正常 0删除
    @TableField(value = "del_flag")
    private Integer delFlag;


    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(value = "create_by")
    private String createBy;


    @TableField(value = "update_by")
    private String updateBy;

    //备注
    @TableField(value = "remark")
    private String remark;

}
