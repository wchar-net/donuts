package net.wchar.donuts.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wchar.donuts.model.po.SysMenuPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 菜单信息
 * @author Elijah
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Schema(name = "菜单信息", description = "菜单信息")
public class MenuBo {

    @Schema(description = "菜单id")
    private Long menuId;

    @Schema(description = "菜单名称")
    private String menuName;


    @Schema(description = "菜单父id")
    private Long parentId;

    @Schema(description = "父菜单名称")
    private String parentMenuName;

    @Schema(description = "菜单显示顺序")
    private Integer orderNum;

    @Schema(description = "菜单url")
    private String url;

    //打开方式（1内部,2新窗口）
    @Schema(description = "打开方式（1内部,2新窗口）")
    private Integer target;


    //菜单类型（1目录 2菜单 3按钮）
    @Schema(description = "菜单类型（1目录 2菜单 3按钮）")
    private Integer menuType;

    //菜单状态（1显示 0隐藏）
    @Schema(description = "菜单状态（1显示 0隐藏）")
    private Integer visible;

    //菜单图标
    @Schema(description = "菜单图标")
    private String icon;

    //备注
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String createBy;


    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "修改人")
    private String updateBy;

    @Schema(description = "delFlag")
    private Integer delFlag;

    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "子集")
    @Builder.Default
    private List<MenuBo> children = new ArrayList<>();


    @JsonIgnore
    public static MenuBo po2bo(SysMenuPo menu) {
        MenuBo menuBo = MenuBo.builder().menuId(menu.getMenuId()).menuName(menu.getMenuName())
                .parentId(menu.getParentId()).orderNum(menu.getOrderNum())
                .url(menu.getUrl()).target(menu.getTarget())
                .createTime(menu.getCreateTime()).perms(menu.getPerms())
                .menuType(menu.getMenuType()).visible(menu.getVisible()).icon(menu.getIcon())
                .remark(menu.getRemark()).build();
        return menuBo;
    }
}
