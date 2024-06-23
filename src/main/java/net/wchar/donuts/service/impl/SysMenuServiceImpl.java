package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.model.po.SysDeptPo;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.sys.holder.UserContextHolder;
import net.wchar.donuts.mapper.SysMenuMapper;
import net.wchar.donuts.model.bo.MenuBo;
import net.wchar.donuts.model.po.SysMenuPo;
import net.wchar.donuts.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单
 * @author Elijah
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenuPo> implements SysMenuService {

    private final SysMenuMapper sysMenuMapper;

    public SysMenuServiceImpl(SysMenuMapper sysMenuMapper) {
        this.sysMenuMapper = sysMenuMapper;
    }

    @Override
    public List<MenuBo> getMenuList(String menuName) {
        List<SysMenuPo> sysMenuList = lambdaQuery().eq(SysMenuPo::getDelFlag, 1)
                .like(StringUtils.hasText(menuName), SysMenuPo::getMenuName, menuName)
                .orderByDesc(SysMenuPo::getOrderNum).list();
        List<MenuBo> menuList = new ArrayList<>();
        for (SysMenuPo po : sysMenuList) {
            menuList.add(MenuBo.po2bo(po));
        }
        //搜索
        if (StringUtils.hasText(menuName)) {
            Map<Long, List<MenuBo>> menuChildrenMap = new HashMap<>();
            List<MenuBo> orphanMenus = new ArrayList<>();

            // 找出每个MenuBo对象的子集
            for (MenuBo menu : menuList) {
                List<MenuBo> children = findChildrenRecursive(menuList, menu.getMenuId());
                if (!children.isEmpty()) {
                    menuChildrenMap.put(menu.getMenuId(), children);
                }
            }

            // 找出没有关联的菜单项
            for (MenuBo menu : menuList) {
                if (menu.getParentId() != null && !menuChildrenMap.containsKey(menu.getParentId())) {
                    orphanMenus.add(menu);
                }
            }
            List<MenuBo> bos = new ArrayList<>();
            menuChildrenMap.forEach((k, v) -> {
                MenuBo menuBo = menuList.stream().filter(t -> t.getMenuId() == k).collect(Collectors.toList()).get(0);
                menuBo.setParentId(0L);
                bos.add(menuBo);
                bos.addAll(v);
            });

            for (int i = 0; i < orphanMenus.size(); i++) {
                MenuBo orp = orphanMenus.get(i);
                for (MenuBo item : bos) {
                    if (item.getMenuId() == orp.getMenuId()) {
                        orphanMenus.remove(orp);
                    }
                }
            }
            orphanMenus.forEach(k ->{
                k.setParentId(0L);
            });
            bos.addAll(orphanMenus);
            return bos;

        }
        return menuList;
    }

    // 递归查找某个MenuBo及其所有子菜单
    private static List<MenuBo> findChildrenRecursive(List<MenuBo> menuList, Long parentId) {
        List<MenuBo> children = new ArrayList<>();
        for (MenuBo menu : menuList) {
            if (parentId.equals(menu.getParentId())) {
                children.add(menu);
                children.addAll(findChildrenRecursive(menuList, menu.getMenuId()));
            }
        }
        return children;
    }

    @Override
    public List<MenuBo> getMenuTree(Long menuId) {
        List<MenuBo> menuTree = sysMenuMapper.getMenuTree(menuId);
        return menuTree;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMenu(Long menuId) {
        Assert.notNull(menuId, "菜单id不能为空!");
        SysMenuPo po = lambdaQuery().eq(SysMenuPo::getDelFlag, 1).eq(SysMenuPo::getMenuId, menuId).one();
        if (null == po || null == po.getMenuId()) {
            throw new BusException("此菜单不存在!");
        }
        Long count = lambdaQuery().eq(SysMenuPo::getDelFlag, 1).eq(SysMenuPo::getParentId, menuId).count();
        if (count > 0) {
            throw new BusException("此菜单存在子集,请先删除子集!");
        }
        return lambdaUpdate().eq(SysMenuPo::getMenuId, menuId).set(SysMenuPo::getUpdateBy,UserContextHolder.getUser().getUserName())
                .set(SysMenuPo::getUpdateTime,LocalDateTime.now())
                .set(SysMenuPo::getDelFlag, 0).update();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editMenu(MenuBo menuBo) {
        Assert.notNull(menuBo.getMenuId(), "菜单ip不能为空!");
        Assert.notNull(menuBo.getMenuType(), "菜单类型不能为空!");
        Assert.notNull(menuBo.getVisible(), "菜单状态不能为空!");
        Assert.notNull(menuBo.getTarget(), "菜单打开方式不能为空!");
        Assert.hasText(menuBo.getMenuName(), "菜单名称不能为空!");

        if (null == menuBo.getParentId()) {
            //顶级菜单父id为0
            menuBo.setParentId(0L);
        }
        SysMenuPo po = SysMenuPo.builder().parentId(menuBo.getParentId()).menuId(menuBo.getMenuId())
                .menuName(menuBo.getMenuName()).menuType(menuBo.getMenuType())
                .target(menuBo.getTarget()).url(menuBo.getUrl()).visible(menuBo.getVisible())
                .perms(menuBo.getPerms()).icon(menuBo.getIcon())
                .orderNum(menuBo.getOrderNum())
                .updateBy(UserContextHolder.getUser().getUserName()).updateTime(LocalDateTime.now()).delFlag(1).build();
        return updateById(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMenu(MenuBo menuBo) {
        Assert.notNull(menuBo.getMenuType(), "菜单类型不能为空!");
        Assert.notNull(menuBo.getVisible(), "菜单状态不能为空!");
        Assert.notNull(menuBo.getTarget(), "菜单打开方式不能为空!");
        Assert.hasText(menuBo.getMenuName(), "菜单名称不能为空!");

        if (null == menuBo.getParentId()) {
            //顶级菜单父id为0
            menuBo.setParentId(0L);
        }
        SysMenuPo po = SysMenuPo.builder().parentId(menuBo.getParentId())
                .menuName(menuBo.getMenuName()).menuType(menuBo.getMenuType())
                .target(menuBo.getTarget()).url(menuBo.getUrl()).visible(menuBo.getVisible())
                .perms(menuBo.getPerms()).icon(menuBo.getIcon())
                .orderNum(menuBo.getOrderNum())
                .createBy(UserContextHolder.getUser().getUserName()).createTime(LocalDateTime.now()).delFlag(1).build();
        return save(po);
    }

    @Override
    public MenuBo getMenuDetail(Long menuId) {
        Assert.notNull(menuId, "菜单id不能为空!");
        SysMenuPo po = lambdaQuery().eq(SysMenuPo::getMenuId, menuId).eq(SysMenuPo::getDelFlag, 1).one();
        Long parentId = po.getParentId();
        MenuBo menuBo = MenuBo.po2bo(po);
        if (null != parentId) {
            //父菜单
            SysMenuPo parentMenu = lambdaQuery().eq(SysMenuPo::getMenuId, parentId).eq(SysMenuPo::getDelFlag, 1).one();
            if (null != parentMenu) {
                menuBo.setParentMenuName(parentMenu.getMenuName());
            }
        }
        return menuBo;
    }
}
