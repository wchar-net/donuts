package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.bo.RoleBo;
import net.wchar.donuts.model.bo.UserBo;
import net.wchar.donuts.model.po.SysMenuPo;
import net.wchar.donuts.model.po.SysUserPo;
import net.wchar.donuts.model.vo.PageUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户
 * @author Elijah
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUserPo> {

    //检查用户是否存在有效部门
    Integer countDeptByUserId(@Param("userId") Long userId);

    //获取用户菜单
    List<SysMenuPo> getUserMenu(@Param("userId") Long userId);

    //判断用户是否有这个菜单的权限
    Long checkMenuPermission(@Param("userId") Long userId, @Param("perms") String perms);

    //获取所有用户菜单权限
    List<String> getUserAllMenuPermission(@Param("userId") Long userId);

    //获取部门信息
    List<DeptBo> getAllDept(@Param("userId") Long userId);

    //查询角色信息
    Page<RoleBo> pageUserRole(Page<RoleBo> page, @Param("roleName") String roleName);

    //分配角色
    Integer assignUserRole(@Param("roleId") List<Long> roleId, @Param("userId") Long userId);

    Integer removeOriginUserRole(@Param("userId") Long userId);

    //获取所有角色信息
    List<RoleBo> getAllRole(@Param("userId") Long userId);

    //分页查询用户信息
    Page<UserBo> pageUser(Page<SysUserPo> page, @Param("vo") PageUserVo vo);

    //获取用户部门
    DeptBo getUserDept(@Param("userId") Long userId);

    //删除用户部门
    Integer removeUserDept(@Param("userId") Long userId, @Param("updateTime") LocalDateTime updateTime, @Param("updateBy") String updateBy);

    //查询用户和部门关系
    Integer insertUserDept(@Param("userId") Long userId, @Param("deptId") Long deptId, @Param("createTime") LocalDateTime createTime, @Param("createBy") String createBy);

    //count部门信息
    Long countDeptById(@Param("deptId") Long deptId);

    //count 用户名
    Long countUserName(@Param("userName") String userName);
}
