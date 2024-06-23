package net.wchar.donuts.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.bo.*;
import net.wchar.donuts.model.po.SysUserPo;
import net.wchar.donuts.model.vo.*;

import java.util.List;

/**
 * 用户
 * @author Elijah
 */
public interface SysUserService extends IService<SysUserPo> {
    //登录
    String login(String userAgent,String loginIp,String userName, String userPwd);

    //获取用户菜单
    List<MenuBo> getUserMenu(LoginBo user, String lang);

    //查询用户列表
    Page<UserBo> pageUser(PageUserVo vo);

    //判断用户是否有这个菜单的权限
    boolean checkMenuPermission(Long userId,String perms);

    //删除用户
    boolean removeUser(Long userId);

    //获取所有用户菜单权限
    List<String> getUserAllMenuPermission(Long userId);

    //获取部门信息
    List<DeptBo> getAllDept(Long userId);

    //获取角色
    Page<RoleBo> pageUserRole(Integer pageIndex, Integer pageSize, String roleName);

    //分配角色
    boolean assignUserRole(AssignUserRoleVo vo);

    //获取所有角色信息
    List<RoleBo> getAllRole(Long userId);

    //查询用户详情
    UserDetailVo getUserDetail(Long userId);

    //修改用户
    boolean modifyUser(UserDetailVo detailVo);

    //修改用户状态
    boolean modifyUserStatus(ModifyUserStatusVo statusVo);

    //重制用户密码
    String resetUserPwd(ResetUserPwdVo vo);
    //添加用户
    Boolean addUser(UserDetailVo vo);

    //修改用户密码
    Boolean editUserPwd(String oldPwd, String newPwd);
}
