package net.wchar.donuts.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 批量删除用户
 * @author Elijah
 */
@Schema(name = "批量删除用户", description = "批量删除用户")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RemoveUserBo {
    @Schema(description = "用户ids")
    private List<Long> userIds;
}
