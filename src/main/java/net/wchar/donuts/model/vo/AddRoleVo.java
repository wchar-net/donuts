package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 添加角色
 * @author Elijah
 */
@Data
@Schema(name = "添加角色", description = "添加角色")
public class AddRoleVo {

    @Schema(description = "角色名称")
    private String roleName;

    //角色状态 1正常 0禁用
    @Schema(description = "角色状态 1正常 0禁用")
    private Integer roleStatus;

    @Schema(description = "菜单id数组")
    private List<Long> menuIds;

}
