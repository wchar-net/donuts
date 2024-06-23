package net.wchar.donuts.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 统一返回值
 * @author Elijah
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Schema(name = "统一返回值", description = "统一返回值")
public class ResultDomain<T> {
    @Schema(description = "统一code,1为正常，其他错误")
    private String code;
    @Schema(description = "msg")
    private String msg;
    @Schema(description = "数据")
    private T data;
}
