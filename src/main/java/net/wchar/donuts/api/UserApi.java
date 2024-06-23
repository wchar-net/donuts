package net.wchar.donuts.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wchar.donuts.sys.annotation.OpLog;
import net.wchar.donuts.sys.annotation.RequiresPermissions;
import net.wchar.donuts.sys.enums.BusinessType;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.model.bo.RemoveUserBo;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.bo.UserBo;
import net.wchar.donuts.model.domain.ResultDomain;
import net.wchar.donuts.model.domain.ResultPageDomain;
import net.wchar.donuts.model.vo.*;
import net.wchar.donuts.service.SysUserService;
import net.wchar.donuts.sys.util.ResultUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口
 * @author Elijah
 */
@Tag(name = "用户接口", description = "用户接口")
@RestController
@RequestMapping("/user")
public class UserApi {
    private final Logger logger = LoggerFactory.getLogger(UserApi.class);

    private final SysUserService sysUserService;


    private final MessageSource messageSource;

    public UserApi(SysUserService sysUserService, MessageSource messageSource) {
        this.sysUserService = sysUserService;
        this.messageSource = messageSource;
    }


    //查询用户列表
    @Operation(summary = "查询用户列表")
    @RequiresPermissions("sys:user:list")
    @GetMapping("/pageUser.action")
    public ResultPageDomain<UserBo> pageUser(@ParameterObject PageUserVo vo) {
        Page<UserBo> page = sysUserService.pageUser(vo);
        return ResultUtils.pageSuccess(page.getTotal(), page.getRecords());
    }

    //删除用户
    @Operation(summary = "删除用户")
    @Parameters({
            @Parameter(name = "userId", description = "用户id"),
    })
    @RequiresPermissions("sys:user:del")
    @OpLog(title = "删除用户", businessType = BusinessType.DELETE)
    @PostMapping("/removeUser.action")
    public ResultDomain<Boolean> removeUser(Long userId) {
        Assert.notNull(userId, "userId不能为空!");
        if (UserContextHolder.getUser().getUserId() == userId) {
            throw new BusException(messageSource.getMessage("not.del.self", null, LocaleContextHolder.getLocale()));
        }
        return ResultUtils.success(messageSource.getMessage("del.success", null, LocaleContextHolder.getLocale()), sysUserService.removeUser(userId));
    }

    @Operation(summary = "批量删除用户")
    @Parameters({
            @Parameter(name = "userId", description = "用户id"),
    })
    @RequiresPermissions("sys:user:del")
    @OpLog(title = "批量删除用户", businessType = BusinessType.DELETE)
    @PostMapping("/batchRemoveUser.action")
    public ResultDomain<Boolean> batchRemoveUser(@RequestBody RemoveUserBo vo) {
        Assert.notNull(vo, "参数不能为空!");
        Assert.notNull(vo.getUserIds(), "userIds不能为空!");
        for (Long userId : vo.getUserIds()) {
            if (UserContextHolder.getUser().getUserId() == userId) {
                throw new BusException(messageSource.getMessage("not.del.self", null, LocaleContextHolder.getLocale()));
            }
            sysUserService.removeUser(userId);
        }
        return ResultUtils.success(messageSource.getMessage("del.success", null, LocaleContextHolder.getLocale()), true);
    }


    //搜索角色信息
    @Operation(summary = "搜索角色信息")
    @Parameters({
            @Parameter(name = "pageIndex", description = "pageIndex"),
            @Parameter(name = "pageSize", description = "pageSize"),
            @Parameter(name = "roleName", description = "角色名称(模糊查询)"),
    })
    @RequiresPermissions("sys:user:role:list")
    @GetMapping("/pageUserRole.action")
    public ResultPageDomain<RoleBo> pageUserRole(Integer pageIndex, Integer pageSize, String roleName) {
        Page<RoleBo> page = sysUserService.pageUserRole(pageIndex, pageSize, roleName);
        return ResultUtils.pageSuccess(page.getTotal(), page.getRecords());
    }

    //分配角色信息
    @Operation(summary = "分配角色信息")
    @RequiresPermissions("sys:user:assign")
    @PostMapping("/assignUserRole.action")
    public ResultDomain<Boolean> assignUserRole(@RequestBody AssignUserRoleVo vo) {
        return ResultUtils.success(sysUserService.assignUserRole(vo));
    }

    //获取某个用户的所有角色
    @Operation(summary = "获取某个用户的所有角色")
    @RequiresPermissions("sys:user:role:list")
    @Parameters({
            @Parameter(name = "userId", description = "用户id")
    })
    @GetMapping("/getUserRole.action")
    public ResultDomain<List<RoleBo>> getUserRole(Long userId) {
        return ResultUtils.success(sysUserService.getAllRole(userId));
    }

    //查询用户详情
    @Operation(summary = "查询用户详情")
    @RequiresPermissions("sys:user:list")
    @GetMapping("/getUserDetail.action")
    public ResultDomain<UserDetailVo> getUserDetail(Long userId) {
        Assert.notNull(userId, "用户id不能为空!");
        return ResultUtils.success(sysUserService.getUserDetail(userId));
    }

    //修改用户
    @Operation(summary = "修改用户")
    @RequiresPermissions("sys:user:edit")
    @PostMapping("/modifyUser.action")
    public ResultDomain<Boolean> getUserRole(@RequestBody UserDetailVo detailVo) {
        return ResultUtils.success(sysUserService.modifyUser(detailVo));
    }

    @Operation(summary = "修改用户状态")
    @RequiresPermissions("sys:user:edit")
    @PostMapping("/modifyUserStatus.action")
    public ResultDomain<Boolean> modifyUserStatus(@RequestBody ModifyUserStatusVo statusVo) {
        return ResultUtils.success(sysUserService.modifyUserStatus(statusVo));
    }

    @Operation(summary = "重制用户密码")
    @RequiresPermissions("sys:user:edit")
    @PostMapping("/resetUserPwd.action")
    public ResultDomain<String> resetUserPwd(@RequestBody ResetUserPwdVo vo) {
        return ResultUtils.success(sysUserService.resetUserPwd(vo));
    }

    //查询用户详情
    @Operation(summary = "添加用户")
    @RequiresPermissions("sys:user:add")
    @PostMapping("/addUser.action")
    public ResultDomain<Boolean> addUser(@RequestBody UserDetailVo vo) {
        return ResultUtils.success(sysUserService.addUser(vo));
    }

    //修改用户密码
    @Operation(summary = "修改用户密码")
    @PostMapping("/editUserPwd.action")
    @Parameters({
            @Parameter(name = "oldPwd", description = "旧密码"),
            @Parameter(name = "newPwd", description = "新密码")
    })
    @OpLog(title = "修改用户密码", businessType = BusinessType.OTHER)
    public ResultDomain<Boolean> editUserPwd(
            String oldPwd,
            String newPwd
    ) {
        return ResultUtils.success(sysUserService.editUserPwd(oldPwd, newPwd));
    }

}
