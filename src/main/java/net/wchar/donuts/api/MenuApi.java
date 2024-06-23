package net.wchar.donuts.api;


import net.wchar.donuts.sys.annotation.OpLog;
import net.wchar.donuts.sys.annotation.RequiresPermissions;
import net.wchar.donuts.sys.enums.BusinessType;
import net.wchar.donuts.model.bo.MenuBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.service.SysMenuService;
import net.wchar.donuts.sys.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单接口
 * @author Elijah
 */
@Tag(name = "菜单接口", description = "菜单接口")
@RestController
@RequestMapping("/menu")
public class MenuApi {

    private final SysMenuService sysMenuService;

    public MenuApi(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    //查询用户列表
    @Operation(summary = "查询用户列表")
    @RequiresPermissions("sys:menu:list")
    @GetMapping("/getMenuList.action")
    @Parameters({
            @Parameter(name = "menuName", description = "菜单名称(模糊查询)"),
    })
    public ResultDomain<List<MenuBo>> getMenuList(String menuName) {
        List<MenuBo> list = sysMenuService.getMenuList(menuName);
        return ResultUtils.success(list);
    }

    //获取菜单树
    @GetMapping("/getMenuTree.action")
    @RequiresPermissions("sys:menu:list")
    @Parameters({
            @Parameter(name = "menuId", description = "菜单id(默认1)"),
    })
    @Operation(summary = "获取菜单树")
    public ResultDomain<List<MenuBo>> getMenuTree(Long menuId) {
        return ResultUtils.success(sysMenuService.getMenuTree(menuId));
    }

    //添加菜单
    @PostMapping("/addMenu.action")
    @RequiresPermissions("sys:menu:add")
    @Operation(summary = "添加菜单")
    public ResultDomain<Boolean> addMenu(@RequestBody MenuBo menuBo) {
        return ResultUtils.success(sysMenuService.addMenu(menuBo));
    }

    //修改菜单
    @PostMapping("/editMenu.action")
    @RequiresPermissions("sys:menu:edit")
    @Operation(summary = "修改菜单")
    public ResultDomain<Boolean> editMenu(@RequestBody MenuBo menuBo) {
        return ResultUtils.success(sysMenuService.editMenu(menuBo));
    }

    //查询菜单详情
    @Operation(summary = "查询菜单详情")
    @Parameters({
            @Parameter(name = "menuId", description = "菜单id"),
    })
    @RequiresPermissions("sys:menu:list")
    @GetMapping("/getMenuDetail.action")
    public ResultDomain<MenuBo> getMenuDetail(Long menuId) {
        return ResultUtils.success(sysMenuService.getMenuDetail(menuId));
    }

    //删除菜单
    @PostMapping("/removeMenu.action")
    @RequiresPermissions("sys:menu:del")
    @Parameters({
            @Parameter(name = "menuId", description = "菜单id"),
    })
    @OpLog(title = "删除菜单", businessType = BusinessType.DELETE)
    @Operation(summary = "删除菜单")
    public ResultDomain<Boolean> removeMenu(Long menuId) {
        return ResultUtils.success(sysMenuService.removeMenu(menuId));
    }


}
