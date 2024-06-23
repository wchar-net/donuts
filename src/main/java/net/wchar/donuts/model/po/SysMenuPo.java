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
 * 菜单
 * @author Elijah
 */
@Data
@Builder
@ToString
@TableName("sys_menu")
public class SysMenuPo {

    @TableId(type = IdType.AUTO, value = "menu_id")
    private Long menuId;

    @TableField(value = "menu_name")
    private String menuName;


    @TableField(value = "parent_id")
    private Long parentId;

    @TableField(value = "order_num")
    private Integer orderNum;

    @TableField(value = "url")
    private String url;

    //打开方式（1内部,2新窗口）
    @TableField(value = "target")
    private Integer target;


    //菜单类型（1目录 2菜单 3按钮）
    @TableField(value = "menu_type")
    private Integer menuType;

    //菜单状态（1显示 0隐藏）
    @TableField(value = "visible")
    private Integer visible;

    //权限标识
    @TableField(value = "perms")
    private String perms;

    //菜单图标
    @TableField(value = "icon")
    private String icon;

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

    //1正常 0删除
    @TableField(value = "del_flag")
    private Integer delFlag;

}
