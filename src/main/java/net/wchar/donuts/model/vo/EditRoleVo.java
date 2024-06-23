package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 修改角色
 * @author Elijah
 */
@Data
@Schema(name = "修改角色", description = "修改角色")
public class EditRoleVo {

    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    //角色状态 1正常 0禁用
    @Schema(description = "角色状态 1正常 0禁用")
    private Integer roleStatus;

    @Schema(description = "菜单id数组")
    private List<Long> menuIds;

}
