package net.wchar.donuts.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 重制用户密码
 * @author Elijah
 */
@Data
@Schema(name = "重制用户密码", description = "重制用户密码")
public class ResetUserPwdVo {

    @Schema(description = "用户id")
    private List<Long> userIds;
}
