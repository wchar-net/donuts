package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wchar.donuts.model.bo.MenuBo;
import net.wchar.donuts.model.po.SysMenuPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单
 * @author Elijah
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenuPo> {
    //获取菜单树
    List<MenuBo> getMenuTree(@Param("menuId") Long menuId);
}
