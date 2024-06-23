package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.api.FileApi;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.mapper.SysLoginMapper;
import net.wchar.donuts.mapper.SysUserMapper;
import net.wchar.donuts.model.bo.*;
import net.wchar.donuts.model.po.SysLoginPo;
import net.wchar.donuts.model.po.SysMenuPo;
import net.wchar.donuts.model.po.SysUserPo;
import net.wchar.donuts.model.vo.*;
import net.wchar.donuts.service.SysUserService;
import net.wchar.donuts.sys.util.DateValidatorUtils;
import net.wchar.donuts.sys.util.PwdUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 用户
 * @author Elijah
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserPo> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLoginMapper sysLoginMapper;


    @Override
    public Page<UserBo> pageUser(PageUserVo vo) {
        Integer pageIndex = null == vo.getPageIndex() ? 1 : vo.getPageIndex();
        Integer pageSize = null == vo.getPageSize() ? 10 : vo.getPageSize();

        if (!StringUtils.hasText(vo.getUserName())) {
            vo.setUserName(null);
        }
        if (!StringUtils.hasText(vo.getUserPhone())) {
            vo.setUserPhone(null);
        }
        if (null == DateValidatorUtils.parseLocalDate(vo.getLastLoginDate())) {
            vo.setLastLoginDate(null);
        }
        if (null == vo.getDeptId()) {
            vo.setDeptId(1L);
        }
        return sysUserMapper.pageUser(new Page<>(pageIndex, pageSize), vo);
    }

    @Override
    public boolean checkMenuPermission(Long userId, String perms) {
        if (!StringUtils.hasText(perms)) {
            return true;
        }
        return sysUserMapper.checkMenuPermission(userId, perms) > 0;
    }

    @Override
    public boolean removeUser(Long userId) {
        Assert.notNull(userId, "用户id不能为空!");
        checkSuperAdmin(userId);
        return lambdaUpdate().eq(SysUserPo::getUserId, userId)
                .set(SysUserPo::getDelFlag, "0")
                .set(SysUserPo::getUpdateBy, UserContextHolder.getUser().getUserName())
                .set(SysUserPo::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    public List<String> getUserAllMenuPermission(Long userId) {
        return sysUserMapper.getUserAllMenuPermission(userId);
    }

    @Override
    public List<DeptBo> getAllDept(Long userId) {
        return sysUserMapper.getAllDept(userId);
    }

    @Override
    public Page<RoleBo> pageUserRole(Integer pageIndex, Integer pageSize, String roleName) {
        if (null == pageIndex) {
            pageIndex = 1;
        }
        if (null == pageSize) {
            pageSize = 10;
        }
        if (!StringUtils.hasText(roleName)) {
            roleName = null;
        }
        return sysUserMapper.pageUserRole(new Page<>(pageIndex, pageSize), roleName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignUserRole(AssignUserRoleVo vo) {
        Assert.notNull(vo, "角色id不能为空!");
        Assert.notNull(vo.getUserId(), "用户id不能为空!");
        if (!CollectionUtils.isEmpty(vo.getRoleIds())) {
            sysUserMapper.removeOriginUserRole(vo.getUserId());
            return sysUserMapper.assignUserRole(vo.getRoleIds(), vo.getUserId()) > 0;
        }
        return Boolean.TRUE;
    }

    @Override
    public List<RoleBo> getAllRole(Long userId) {
        Long count = lambdaQuery().eq(SysUserPo::getUserId, userId).eq(SysUserPo::getDelFlag, 1).count();
        if (count <= 0) {
            throw new BusException("用户不存在!");
        }
        return sysUserMapper.getAllRole(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyUserStatus(ModifyUserStatusVo statusVo) {
        Assert.notNull(statusVo.getUserIds(), "userIds不能为空!");
        Assert.notNull(statusVo.getStatus(), "状态不能为空!");
        if (1 != statusVo.getStatus() && 0 != statusVo.getStatus()) {
            throw new BusException("状态字段格式不争取! 1启用 0禁用");
        }
        for (Long userId : statusVo.getUserIds()) {
            checkSuperAdmin(userId);
            lambdaUpdate().eq(SysUserPo::getUserId, userId).eq(SysUserPo::getDelFlag, 1)
                    .set(SysUserPo::getUserStatus, statusVo.getStatus())
                    .set(SysUserPo::getUpdateBy, UserContextHolder.getUser().getUserName())
                    .set(SysUserPo::getUpdateTime, LocalDateTime.now())
                    .update();
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addUser(UserDetailVo vo) {
        Assert.hasText(vo.getUserName(), "用户名不能为空!");
        Assert.notNull(vo.getUserType(), "用户类型不能为空!");
        vo.setUserId(null);
        if (vo.getUserType() != 0 && vo.getUserType() != 1) {
            throw new BusException("用户类型格式不正确 (0内部 1外部)!");
        }

        //（0男 1女 2未知）
        Assert.notNull(vo.getUserSex(), "用户性别不能为空!");
        if (vo.getUserSex() != 0 && vo.getUserSex() != 1 && vo.getUserSex() != 2) {
            throw new BusException("用户性别格式不正确 （0男 1女 2未知）!");
        }
        if (!StringUtils.hasText(vo.getUserAvatar())) {
            //默认头像
            vo.setUserAvatar(FileApi.DEFAULT_AVATAR);
        }

        //帐号状态 (1正常 0停用)
        Assert.notNull(vo.getUserStatus(), "用户状态不能为空!");
        if (vo.getUserStatus() != 0 && vo.getUserStatus() != 1) {
            throw new BusException("帐号状态格式不正确 (1正常 0停用)!");
        }

        Assert.hasText(vo.getUserPwd(), "用户密码不能为空!");
        vo.setUserPwd(PwdUtil.encoder(vo.getUserPwd()));

        //检查部门信息
        Assert.notNull(vo.getUserDeptId(), "部门id不能为空!");
        Long count = sysUserMapper.countDeptById(vo.getUserDeptId());
        if (count <= 0) {
            throw new BusException("此部门不存在!");
        }
        //检查用户名称不能重复
        if (sysUserMapper.countUserName(vo.getUserName()) > 0) {
            throw new BusException("此用户名称已经存在!");
        }

        vo.setUserAvatar(vo.getUserAvatarReal());
        if (!StringUtils.hasText(vo.getUserAvatar())) {
            //默认头像
            vo.setUserAvatar(FileApi.DEFAULT_AVATAR);
        }

        LocalDateTime createOrUpdateTime = LocalDateTime.now();
        LoginBo createOrUpdateUser = UserContextHolder.getUser();

        SysUserPo sysUserPo = UserDetailVo.vo2po(vo);
        sysUserPo.setCreateTime(createOrUpdateTime);
        sysUserPo.setCreateBy(createOrUpdateUser.getUserName());

        sysUserPo.setDelFlag(1);
        sysUserPo.setUpdateTime(null);
        sysUserPo.setUpdateBy(null);
        sysUserPo.setLastPwdUpdateDate(null);
        sysUserPo.setLastLoginIp(null);
        sysUserPo.setLastPwdUpdateDate(null);


        sysUserMapper.insert(sysUserPo);
        if (null == sysUserPo.getUserId()) {
            throw new BusException("保存出错!数据库未返回主键id!");
        }
        sysUserMapper.insertUserDept(sysUserPo.getUserId(), vo.getUserDeptId(), createOrUpdateTime, createOrUpdateUser.getUserName());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editUserPwd(String oldPwd, String newPwd) {
        Assert.notNull(oldPwd, "旧密码不能为空!");
        Assert.notNull(oldPwd, "新密码不能为空!");
        SysUserPo userPo = lambdaQuery().eq(SysUserPo::getDelFlag, 1).eq(SysUserPo::getUserId, UserContextHolder.getUser().getUserId()).one();
        if (!PwdUtil.matches(oldPwd, userPo.getUserPwd())) {
            throw new BusException("原密码错误!");
        }
        return lambdaUpdate().eq(SysUserPo::getUserId, UserContextHolder.getUser().getUserId())
                .set(SysUserPo::getUserPwd, PwdUtil.encoder(newPwd))
                .set(SysUserPo::getUpdateBy, UserContextHolder.getUser().getUserName())
                .set(SysUserPo::getUpdateTime, LocalDateTime.now())
                .update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetUserPwd(ResetUserPwdVo vo) {
        Assert.notNull(vo.getUserIds(), "userId不能为空!");
        String password = PwdUtil.generatePassword(8, true, true, true, true);
        for (Long userId : vo.getUserIds()) {
            checkSuperAdmin(userId);
            lambdaUpdate().eq(SysUserPo::getUserId, userId).eq(SysUserPo::getDelFlag, 1)
                    .set(SysUserPo::getUserPwd, PwdUtil.encoder(password))
                    .set(SysUserPo::getUpdateBy, UserContextHolder.getUser().getUserName())
                    .set(SysUserPo::getUpdateTime, LocalDateTime.now())
                    .update();

        }
        return password;
    }

    //超级管理员不能被修改
    private void checkSuperAdmin(Long userId) {
        if (1 == userId) {
            //超级管理员不能被删除
            throw new BusException("此用户不允许被操作 userId = 1!");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyUser(UserDetailVo detailVo) {
        Assert.notNull(detailVo.getUserId(), "userId不能为空!");
        SysUserPo sysUserPo = UserDetailVo.vo2po(detailVo);
        sysUserPo.setUserAvatar(detailVo.getUserAvatarReal());

        checkSuperAdmin(detailVo.getUserId());

        //此字段是唯一的,不能被修改
        sysUserPo.setUserName(null);
        sysUserPo.setCreateBy(null);
        sysUserPo.setCreateTime(null);
        sysUserPo.setLastLoginDate(null);
        sysUserPo.setLastLoginIp(null);
        sysUserPo.setLastPwdUpdateDate(null);

        sysUserPo.setUpdateBy(UserContextHolder.getUser().getUserName());
        sysUserPo.setUpdateTime(LocalDateTime.now());

        if (null != detailVo.getUserDeptId()) {
            //修改用户部门
            sysUserMapper.removeUserDept(detailVo.getUserId(), LocalDateTime.now(), UserContextHolder.getUser().getUserName());
            sysUserMapper.insertUserDept(detailVo.getUserId(), detailVo.getUserDeptId(), LocalDateTime.now(), UserContextHolder.getUser().getUserName());
        }

        return sysUserMapper.updateById(sysUserPo) > 0;
    }


    //头像处理
    private String handlerUserAvatar(String userAvatar) {
        if (StringUtils.hasText(userAvatar)) {
            //头像前缀
            return FileApi.FILE_PREFIX + "/" + userAvatar;
        } else {
            //默认头像
            return FileApi.DEFAULT_AVATAR;
        }
    }

    @Override
    public UserDetailVo getUserDetail(Long userId) {
        SysUserPo userPo = lambdaQuery().eq(SysUserPo::getDelFlag, 1).eq(SysUserPo::getUserId, userId).one();
        if (null == userPo || null == userPo.getUserId()) {
            throw new BusException("此用户不存在!");
        } else {
            UserDetailVo detailVo = UserDetailVo.po2vo(userPo);
            DeptBo deptBo = sysUserMapper.getUserDept(userId);
            detailVo.setDept(deptBo);
            String handlerUserAvatar = handlerUserAvatar(detailVo.getUserAvatar());
            detailVo.setUserAvatar(handlerUserAvatar);
            return detailVo;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String login(String userAgent, String loginIp, String userName, String userPwd) {
        Assert.hasText(userName, "用户名不能为空!");
        Assert.hasText(userName, "密码不能为空!");
        SysUserPo userPo = lambdaQuery().eq(SysUserPo::getDelFlag, 1).eq(SysUserPo::getUserName, userName).one();
        if (null == userPo || null == userPo.getUserId()) {
            throw new BusException("用户不存在!");
        }
        //帐号状态 (1正常 0停用)
        Integer userStatus = userPo.getUserStatus();
        if (1 != userStatus) {
            throw new BusException("此账号已被停用!");
        }

        if (!PwdUtil.matches(userPwd, userPo.getUserPwd())) {
            throw new BusException("账号密码不正确!");
        }
        //检查部门
        Integer count = sysUserMapper.countDeptByUserId(userPo.getUserId());
        if (count <= 0) {
            throw new BusException("此账号无有效部门!");
        }

        String xToken = UUID.randomUUID().toString().replaceAll("-", "");
        LocalDateTime createOrUpdateTime = LocalDateTime.now();
        SysLoginPo loginPo = SysLoginPo.builder().userId(userPo.getUserId())
                .xToken(xToken)
                .loginTime(createOrUpdateTime)
                .userAgent(userAgent)
                .loginIp(loginIp)
                //2小时
                .loginExpTime(createOrUpdateTime.plusHours(2))
                .loginStatus(1)
                .createTime(createOrUpdateTime)
                .createBy("SysUserServiceImpl->login")
                .updateTime(createOrUpdateTime)
                .delFlag(1).build();
        sysLoginMapper.insert(loginPo);
        lambdaUpdate().eq(SysUserPo::getUserId, userPo.getUserId())
                .set(SysUserPo::getLastLoginIp, loginIp)
                .set(SysUserPo::getLastLoginDate, createOrUpdateTime)
                .set(SysUserPo::getUpdateBy, "SysUserServiceImpl->login")
                .set(SysUserPo::getUpdateTime, createOrUpdateTime)
                .update();
        return xToken;
    }

    @Override
    public List<MenuBo> getUserMenu(LoginBo user, String lang) {
        List<SysMenuPo> userMenu = sysUserMapper.getUserMenu(user.getUserId());
        List<MenuBo> menuBos = new ArrayList<>();
        for (SysMenuPo menu : userMenu) {
            menuBos.add(MenuBo.po2bo(menu));
        }
        List<MenuBo> tree = buildMenuTree(menuBos);
        return tree;
    }


    //遍历菜单树结构
    public static List<MenuBo> buildMenuTree(List<MenuBo> menuList) {
        Map<Long, MenuBo> menuMap = new HashMap<>();
        for (MenuBo menu : menuList) {
            menuMap.put(menu.getMenuId(), menu);
        }
        // 构建树状结构
        List<MenuBo> tree = new ArrayList<>();
        for (MenuBo menu : menuList) {
            if (menu.getParentId() == 0) {
                tree.add(menu);
            } else {
                MenuBo parentMenu = menuMap.get(menu.getParentId());
                if (parentMenu != null) {
                    parentMenu.getChildren().add(menu);
                }
            }
        }
        return tree;
    }
}
