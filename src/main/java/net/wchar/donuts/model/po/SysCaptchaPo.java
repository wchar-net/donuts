package net.wchar.donuts.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 验证码
 * @author Elijah
 */
@TableName("sys_captcha")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
public class SysCaptchaPo {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("ip")
    private String ip;

    @TableField("uri")
    private String uri;

    @TableField("method")
    private String method;

    @TableField("user_agent")
    private String userAgent;

    @TableField("captcha_key")
    private String captchaKey;


    @TableField("captcha_code")
    private String captchaCode;

    /**
     * 1正常 0失效
     */
    @TableField("captcha_status")
    private Integer captchaStatus;


    @TableField("captcha_exp_time")
    private LocalDateTime captchaExpTime;

    @TableField("captcha_data")
    private String captchaData;

    @TableField("captcha_img_base64")
    private String captchaImgBase64;

    @TableField("create_time")
    private LocalDateTime createTime;
}
