package net.wchar.donuts.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


/**
 * 访问量
 * @author Elijah
 */
@Schema(name = "top 10 uri 访问量统计", description = "top 10 uri 访问量统计")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccessStatisticsBo {

    @Schema(description = "uri")
    private String uri;

    @Schema(description = "count")
    private Long count;

    @Schema(description = "ip")
    private String ip;

}
