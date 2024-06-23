package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 修改角色状态
 * @author Elijah
 */
@Data
@Schema(name = "修改角色状态", description = "修改角色状态")
public class ModifyRoleStatusVo {

    @Schema(description = "角色id")
    private List<Long> roleIds;

    @Schema(description = "状态 1正常 0停用")
    private Integer status;

}
