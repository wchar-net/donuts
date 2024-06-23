package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分配菜单
 * @author Elijah
 */
@Data
@Schema(name = "分配菜单", description = "分配菜单")
public class AssignRoleMenuVo {
    @Schema(description = "角色id")
    private Long roleId;

    @Schema(description = "菜单id数组")
    private List<Long> menuIds;

}
