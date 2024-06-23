package net.wchar.donuts.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wchar.donuts.model.vo.AddRoleVo;
import net.wchar.donuts.model.vo.EditRoleVo;
import net.wchar.donuts.sys.annotation.OpLog;
import net.wchar.donuts.sys.annotation.RequiresPermissions;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.model.domain.ResultPageDomain;
import net.wchar.donuts.model.vo.AssignRoleMenuVo;
import net.wchar.donuts.model.vo.ModifyRoleStatusVo;
import net.wchar.donuts.service.SysRoleService;
import net.wchar.donuts.sys.enums.BusinessType;
import net.wchar.donuts.sys.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * 角色接口
 * @author Elijah
 */
@Tag(name = "角色接口", description = "角色接口")
@RestController
@RequestMapping("/role")
public class RoleApi {

    private  final SysRoleService sysRoleService;

    public RoleApi(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    //分页查询角色信息
    @Operation(summary = "分页查询角色信息")
    @RequiresPermissions("sys:role:list")
    @GetMapping("/pageRole.action")
    @Parameters({
            @Parameter(name = "pageIndex", description = "pageIndex"),
            @Parameter(name = "pageSize", description = "pageSize"),
            @Parameter(name = "roleName", description = "角色名称(模糊查询)"),
    })
    public ResultPageDomain<RoleBo> pageRole(
            Integer pageIndex,
            Integer pageSize,
            String roleName
    ) {
        Page<RoleBo> page = sysRoleService.pageRole(pageIndex,pageSize,roleName);
        return ResultUtils.pageSuccess(page.getTotal(), page.getRecords());
    }

    @Operation(summary = "修改角色状态")
    @RequiresPermissions("sys:role:edit")
    @PostMapping("/modifyRoleStatus.action")
    public ResultDomain<Boolean> modifyRoleStatus(@RequestBody ModifyRoleStatusVo statusVo) {
        return ResultUtils.success(sysRoleService.modifyUserStatus(statusVo));
    }

    @OpLog(title = "批量删除角色", businessType = BusinessType.DELETE)
    @Operation(summary = "批量删除角色")
    @RequiresPermissions("sys:role:del")
    @PostMapping("/batchRemoveRole.action")
    public ResultDomain<Boolean> batchRemoveRole(@RequestBody ModifyRoleStatusVo statusVo) {
        return ResultUtils.success(sysRoleService.batchRemoveRole(statusVo));
    }

    //分配菜单
    @Operation(summary = "分配菜单")
    @RequiresPermissions("sys:role:assign")
    @PostMapping("/assignRoleMenu.action")
    public ResultDomain<Boolean> assignRoleMenu(@RequestBody AssignRoleMenuVo vo) {
        return ResultUtils.success(sysRoleService.assignRoleMenu(vo));
    }



    //获取角色详情
    @Operation(summary = "获取角色详情")
    @RequiresPermissions("sys:role:list")
    @GetMapping("/getRoleDetail.action")
    @Parameters({
            @Parameter(name = "roleId", description = "角色id")
    })
    public ResultDomain<RoleBo> getRoleDetail(
            Long roleId
    ) {
        RoleBo bo = sysRoleService.getRoleDetail(roleId);
        return ResultUtils.success(bo);
    }

    //添加角色
    @Operation(summary = "添加角色")
    @RequiresPermissions("sys:role:add")
    @PostMapping("/addRole.action")
    public ResultDomain<Boolean> addRole(@RequestBody AddRoleVo vo) {
        return ResultUtils.success(sysRoleService.addRole(vo));
    }

    //修改角色
    @Operation(summary = "修改角色")
    @RequiresPermissions("sys:role:edit")
    @PostMapping("/editRole.action")
    public ResultDomain<Boolean> editRole(@RequestBody EditRoleVo vo) {
        return ResultUtils.success(sysRoleService.editRole(vo));
    }
}
