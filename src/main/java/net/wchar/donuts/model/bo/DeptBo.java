package net.wchar.donuts.model.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.wchar.donuts.model.po.SysDeptPo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 部门信息
 * @author Elijah
 */
@Schema(name = "部门信息", description = "部门信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DeptBo {

    @Schema(description = "部门id")
    private Long deptId;

    @Schema(description = "部门父id")
    private Long deptParentId;


    @Schema(description = "部门父级名称")
    private String deptParentName;

    @Schema(description = "部门编码")
    private String deptCode;

    @Schema(description = "部门名称")
    private String deptName;

    //负责人
    @Schema(description = "部门负责人")
    private String deptLeader;


    @Schema(description = "部门电话")
    private String deptPhone;


    @Schema(description = "部门邮箱")
    private String deptEmail;

    //1正常 0停用
    @Schema(description = "部门状态 1正常 0停用")
    private Integer deptStatus;

    @Schema(description = "子集")
    private List<DeptBo> children;


    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonIgnore
    public static DeptBo po2bo(SysDeptPo po) {
        return DeptBo.builder().deptStatus(po.getDeptStatus())
                .deptEmail(po.getDeptEmail()).deptPhone(po.getDeptPhone()).deptLeader(po.getDeptLeader())
                .deptName(po.getDeptName()).deptCode(po.getDeptCode()).createTime(po.getCreateTime())
                .deptParentId(po.getDeptParentId()).deptId(po.getDeptId()).build();
    }

    @JsonIgnore
    public static SysDeptPo bo2po(DeptBo bo) {
        return SysDeptPo.builder().deptStatus(bo.getDeptStatus())
                .deptEmail(bo.getDeptEmail()).deptPhone(bo.getDeptPhone()).deptLeader(bo.getDeptLeader())
                .deptName(bo.getDeptName()).deptCode(bo.getDeptCode()).createTime(bo.getCreateTime())
                .deptParentId(bo.getDeptParentId()).deptId(bo.getDeptId()).build();
    }

}
