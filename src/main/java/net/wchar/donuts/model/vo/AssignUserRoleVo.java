package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分配角色
 * @author Elijah
 */
@Data
@Schema(name = "分配角色", description = "分配角色")
public class AssignUserRoleVo {
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "角色id数组")
    private List<Long> roleIds;

}
