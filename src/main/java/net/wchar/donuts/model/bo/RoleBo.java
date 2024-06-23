package net.wchar.donuts.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色信息
 * @author Elijah
 */
@Schema(name = "角色信息", description = "角色信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RoleBo {
    @Schema(description = "角色id")
    private  Long roleId;

    @Schema(description = "角色名称")
    private String roleName;


    @Schema(description = "1正常 0禁用")
    private Integer status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "角色拥有的菜单ids")
    private List<Long> menuIds;
}
