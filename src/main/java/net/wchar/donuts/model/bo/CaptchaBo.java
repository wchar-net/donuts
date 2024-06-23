package net.wchar.donuts.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 获取验证码
 * @author Elijah
 */
@Schema(name = "获取验证码", description = "获取验证码")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CaptchaBo {

    @Schema(description = "captchaKey")
    private String captchaKey;

    @Schema(description = "imageBase64")
    private String imageBase64;
}
