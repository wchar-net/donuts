package net.wchar.donuts.api;

import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.service.SysDeptService;
import net.wchar.donuts.sys.annotation.OpLog;
import net.wchar.donuts.sys.annotation.RequiresPermissions;
import net.wchar.donuts.sys.enums.BusinessType;
import net.wchar.donuts.sys.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门接口
 * @author Elijah
 */
@Tag(name = "部门接口", description = "部门接口")
@RestController
@RequestMapping("/dept")
public class DeptApi {

    private final SysDeptService sysDeptService;


    public DeptApi(SysDeptService sysDeptService) {
        this.sysDeptService = sysDeptService;
    }


    @Operation(summary = "获取部门列表")
    @Parameters({
            @Parameter(name = "deptName", description = "部门名称(模糊查询)"),
    })
    @GetMapping("/getDeptList.action")
    public ResultDomain<List<DeptBo>> getDeptList(String deptName) {
        return ResultUtils.success(sysDeptService.getDeptList(deptName));
    }

    @GetMapping("/getDeptTree.action")
    @Parameters({
            @Parameter(name = "deptId", description = "部门id(默认1)"),
    })
    @Operation(summary = "获取部门列表树结构")
    public ResultDomain<DeptBo> getDeptTree(Long deptId) {
        return ResultUtils.success(sysDeptService.getDeptTree(deptId));
    }

    //添加部门
    @PostMapping("/addDept.action")
    @RequiresPermissions("sys:dept:add")
    @Operation(summary = "添加部门")
    public ResultDomain<Boolean> addMenu(@RequestBody DeptBo deptBo) {
        return ResultUtils.success(sysDeptService.addMenu(deptBo));
    }

    //删除部门
    @PostMapping("/removeDept.action")
    @RequiresPermissions("sys:dept:del")
    @Parameters({
            @Parameter(name = "deptId", description = "部门id"),
    })
    @OpLog(title = "删除部门", businessType = BusinessType.DELETE)
    @Operation(summary = "删除部门")
    public ResultDomain<Boolean> removeDept(Long deptId) {
        return ResultUtils.success(sysDeptService.removeDept(deptId));
    }

    //修改部门
    @PostMapping("/editDept.action")
    @RequiresPermissions("sys:dept:edit")
    @Operation(summary = "删除部门")
    public ResultDomain<Boolean> editDept(@RequestBody DeptBo deptBo) {
        return ResultUtils.success(sysDeptService.editDept(deptBo));
    }

    //获取部门详情
    @GetMapping("/getDeptDetail.action")
    @RequiresPermissions("sys:dept:list")
    @Operation(summary = "获取部门详情")
    @Parameters({
            @Parameter(name = "deptId", description = "部门id"),
    })
    public ResultDomain<DeptBo> getDeptDetail(Long deptId) {
        return ResultUtils.success(sysDeptService.getDeptDetail(deptId));
    }
}
