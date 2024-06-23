package net.wchar.donuts.service;


import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.bo.MenuBo;
import net.wchar.donuts.model.po.SysMenuPo;

import java.util.List;

/**
 * 菜单
 * @author Elijah
 */
public interface SysMenuService extends IService<SysMenuPo> {
    //分页菜单
    List<MenuBo> getMenuList(String menuName);

    //获取菜单树
    List<MenuBo> getMenuTree(Long parentId);

    //添加菜单
    Boolean addMenu(MenuBo menuBo);

    //查询菜单详情
    MenuBo getMenuDetail(Long menuId);
    //修改菜单
    Boolean editMenu(MenuBo menuBo);

    //删除菜单
    Boolean removeMenu(Long menuId);
}
