package net.wchar.donuts.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 统一分页返回值
 * @author Elijah
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Schema(name = "统一分页返回值", description = "统一分页返回值")
public class ResultPageDomain<T> {
    @Schema(description = "统一code,1为正常，其他错误")
    private String code;
    @Schema(description = "msg")
    private String msg;
    @Schema(description = "数据总条数")
    private Long total;
    @Schema(description = "数据")
    private List<T> data;
}
