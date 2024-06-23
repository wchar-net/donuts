package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 修改用户状态
 * @author Elijah
 */
@Data
@Schema(name = "修改用户状态", description = "修改用户状态")
public class ModifyUserStatusVo {

    @Schema(description = "用户id")
    private List<Long> userIds;

    @Schema(description = "状态 1启用 0禁用")
    private Integer status;

}
