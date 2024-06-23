package net.wchar.donuts.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.po.SysRolePo;
import net.wchar.donuts.model.vo.AddRoleVo;
import net.wchar.donuts.model.vo.AssignRoleMenuVo;
import net.wchar.donuts.model.vo.EditRoleVo;
import net.wchar.donuts.model.vo.ModifyRoleStatusVo;

/**
 * 角色
 * @author Elijah
 */
public interface SysRoleService extends IService<SysRolePo> {

    //分页查询角色信息
    Page<RoleBo> pageRole(Integer pageIndex, Integer pageSize, String roleName);

    //修改角色状态
    Boolean modifyUserStatus(ModifyRoleStatusVo statusVo);

    //批量删除角色
    Boolean batchRemoveRole(ModifyRoleStatusVo statusVo);

    //分配角色菜单
    Boolean assignRoleMenu(AssignRoleMenuVo vo);

    //获取角色详情
    RoleBo getRoleDetail(Long roleId);

    //添加角色
    Boolean addRole(AddRoleVo vo);

    //修改角色
    Boolean editRole(EditRoleVo vo);
}
