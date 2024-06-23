package net.wchar.donuts.model.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;


/**
 * 访问量
 * @author Elijah
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
@Accessors(chain = true)
@TableName("sys_access_statistics")
public class AccessStatisticsPo {
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

    @TableField("create_time")
    private LocalDateTime createTime;

}
