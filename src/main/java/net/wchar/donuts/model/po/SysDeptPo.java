package net.wchar.donuts.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门
 * @author Elijah
 */
@Builder
@Data
@TableName("sys_dept")
public class SysDeptPo {

    @TableId(type = IdType.AUTO, value = "dept_id")
    private Long deptId;

    @TableField("dept_parent_id")
    private Long deptParentId;

    @TableField("dept_code")
    private String deptCode;

    @TableField("dept_name")
    private String deptName;

    //负责人
    @TableField("dept_leader")
    private String deptLeader;


    @TableField("dept_phone")
    private String deptPhone;


    @TableField("dept_email")
    private String deptEmail;

    //1正常 0停用
    @TableField("dept_status")
    private Integer deptStatus;

    @TableField("create_by")
    private String createBy;


    @TableField("update_by")
    private String updateBy;
    @TableField("create_time")
    private LocalDateTime createTime;


    @TableField("update_time")
    private LocalDateTime updateTime;

    //1正常 0删除
    @TableField("del_flag")
    private Integer delFlag;


}
