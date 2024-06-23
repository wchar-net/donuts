package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.model.po.SysDeptPo;
import net.wchar.donuts.model.vo.AddRoleVo;
import net.wchar.donuts.model.vo.EditRoleVo;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.mapper.SysRoleMapper;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.po.SysRolePo;
import net.wchar.donuts.model.vo.AssignRoleMenuVo;
import net.wchar.donuts.model.vo.ModifyRoleStatusVo;
import net.wchar.donuts.service.SysRoleService;
import net.wchar.donuts.sys.holder.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色
 * @author Elijah
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRolePo> implements SysRoleService {


    private final SysRoleMapper sysRoleMapper;

    public SysRoleServiceImpl(SysRoleMapper sysRoleMapper) {
        this.sysRoleMapper = sysRoleMapper;
    }

    @Override
    public Page<RoleBo> pageRole(Integer pageIndex, Integer pageSize, String roleName) {
        pageIndex = pageIndex != null ? pageIndex : 1;
        pageSize = pageSize != null ? pageSize : 10;
        if (!StringUtils.hasText(roleName)) {
            roleName = null;
        }
        return sysRoleMapper.pageRole(new Page<>(pageIndex, pageSize), roleName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyUserStatus(ModifyRoleStatusVo statusVo) {
        Assert.notNull(statusVo.getStatus(), "状态不能为空");
        Assert.notNull(statusVo.getRoleIds(), "角色id不能为空");
        if (1 != statusVo.getStatus() && 0 != statusVo.getStatus()) {
            throw new BusException("状态格式不正确 1正常 0停用");
        }

        for (Long roleId : statusVo.getRoleIds()) {
            lambdaUpdate().eq(SysRolePo::getDelFlag, 1).eq(SysRolePo::getRoleId, roleId).set(SysRolePo::getStatus, statusVo.getStatus()).update();
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchRemoveRole(ModifyRoleStatusVo statusVo) {
        Assert.notNull(statusVo.getRoleIds(), "角色id不能为空");
        LocalDateTime updateTime = LocalDateTime.now();
        for (Long roleId : statusVo.getRoleIds()) {
            lambdaUpdate().eq(SysRolePo::getRoleId, roleId).set(SysRolePo::getDelFlag, 0)
                    .set(SysRolePo::getUpdateBy,UserContextHolder.getUser().getUserName())
                    .set(SysRolePo::getUpdateTime,updateTime)
                    .update();
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean assignRoleMenu(AssignRoleMenuVo vo) {
        Assert.notNull(vo, "角色id不能为空!");
        Assert.notNull(vo.getMenuIds(), "菜单id不能为空!");
        sysRoleMapper.removeOriginRoleMenu(vo.getRoleId());
        if (!CollectionUtils.isEmpty(vo.getMenuIds())) {
            return sysRoleMapper.assignRoleMenu(vo.getRoleId(), vo.getMenuIds()) > 0;
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addRole(AddRoleVo vo) {
        Assert.hasText(vo.getRoleName(), "角色名称不能为空!");
        Assert.notNull(vo.getRoleStatus(), "角色状态不能为空");
        if (1 != vo.getRoleStatus() && 0 != vo.getRoleStatus()) {
            throw new BusException("状态格式不正确 1正常 0停用");
        }

        SysRolePo po = SysRolePo.builder().roleName(vo.getRoleName())
                .createBy(UserContextHolder.getUser().getUserName())
                .createTime(LocalDateTime.now()).delFlag(1).status(vo.getRoleStatus()).build();
        save(po);
        if (null == po.getRoleId()) {
            throw new BusException("数据库未返回数据id!");
        }
        if (!CollectionUtils.isEmpty(vo.getMenuIds())) {
            return sysRoleMapper.assignRoleMenu(po.getRoleId(), vo.getMenuIds()) > 0;
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editRole(EditRoleVo vo) {
        Assert.notNull(vo.getRoleId(), "角色id不能为空!");
        Assert.hasText(vo.getRoleName(), "角色名称不能为空!");
        Assert.notNull(vo.getRoleStatus(), "角色状态不能为空");
        if (1 != vo.getRoleStatus() && 0 != vo.getRoleStatus()) {
            throw new BusException("状态格式不正确 1正常 0停用");
        }
        SysRolePo po = SysRolePo.builder().roleName(vo.getRoleName())
                .roleId(vo.getRoleId())
                .updateBy(UserContextHolder.getUser().getUserName())
                .updateTime(LocalDateTime.now()).status(vo.getRoleStatus()).build();
        updateById(po);
        if (null == po.getRoleId()) {
            throw new BusException("数据库未返回数据id!");
        }
        sysRoleMapper.removeOriginRoleMenu(vo.getRoleId());
        if (!CollectionUtils.isEmpty(vo.getMenuIds())) {
            return sysRoleMapper.assignRoleMenu(po.getRoleId(), vo.getMenuIds()) > 0;
        }
        return Boolean.TRUE;
    }

    @Override
    public RoleBo getRoleDetail(Long roleId) {
        Assert.notNull(roleId, "角色id不能为空!");
        SysRolePo rolePo = lambdaQuery().eq(SysRolePo::getDelFlag, 1).eq(SysRolePo::getRoleId, roleId).one();
        if (null == rolePo || null == rolePo.getRoleId()) {
            throw new BusException("此角色不存在!");
        }
        //获取角色绑定的菜单ids
        List<Long> menuIds = sysRoleMapper.geBindMenuIds(rolePo.getRoleId());
        return RoleBo.builder().roleName(rolePo.getRoleName()).menuIds(menuIds)
                .roleId(rolePo.getRoleId()).status(rolePo.getStatus()).createTime(rolePo.getCreateTime()).build();
    }

}
