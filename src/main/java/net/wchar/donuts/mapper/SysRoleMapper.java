package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.po.SysRolePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色
 * @author Elijah
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRolePo> {
    //分页查询角色信息
    Page<RoleBo> pageRole(Page<RoleBo> objectPage, @Param("roleName") String roleName);

    //删除原有的角色和菜单对应关系
    Integer removeOriginRoleMenu(@Param("roleId") Long roleId);

    //分配角色菜单
    Integer assignRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    //获取角色绑定的菜单ids
    List<Long> geBindMenuIds(@Param("roleId") Long roleId);
}
