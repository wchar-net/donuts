package net.wchar.donuts.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 日志
 * @author Elijah
 */
@Data
@Builder
@ToString
@TableName("sys_op_log")
@NoArgsConstructor
@AllArgsConstructor
public class SysOpLogPo {
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    @TableId(type = IdType.AUTO,value = "op_id")
    private Long opId;

    /** 操作模块 */
    @TableField("title")
    private String title;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    @TableField("business_type")
    private Integer businessType;

    /** 请求方法 */
    @TableField("method")
    private String method;

    /** 请求方式 */
    @TableField("request_method")
    private String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    @TableField("op_type")
    private Integer opType;


    /** 请求url */
    @TableField("op_url")
    private String opUrl;

    /** 操作地址 */
    @TableField("op_ip")
    private String opIp;


    @TableField("op_user")
    private String opUser;

    @TableField("op_user_id")
    private Long opUserId;

    /** 请求参数 */
    @TableField("op_param")
    private String opParam;

    /** 返回参数 */
    @TableField("json_result")
    private String jsonResult;

    /** 操作状态（0正常 1异常） */
    @TableField("status")
    private Integer status;

    /** 错误消息 */
    @TableField("error_msg")
    private String errorMsg;

    /** 操作时间 */
    @TableField("op_time")
    private LocalDateTime opTime;

    /** 消耗时间 */
    @TableField("cost_time")
    private Long costTime;
}
