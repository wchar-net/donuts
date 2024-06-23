package net.wchar.donuts.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.wchar.donuts.mapper.SysDeptMapper;
import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.bo.MenuBo;
import net.wchar.donuts.model.po.SysDeptPo;
import net.wchar.donuts.model.po.SysMenuPo;
import net.wchar.donuts.service.SysDeptService;
import net.wchar.donuts.sys.exception.BusException;
import net.wchar.donuts.sys.holder.UserContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门
 * @author Elijah
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDeptPo> implements SysDeptService {

    private final SysDeptMapper sysDeptMapper;

    public SysDeptServiceImpl(SysDeptMapper sysDeptMapper) {
        this.sysDeptMapper = sysDeptMapper;
    }


    @Override
    public DeptBo getDeptTree(Long deptId) {
        if (null == deptId) {
            deptId = 1L;
        }
        return sysDeptMapper.getDeptTree(deptId);
    }

    @Override
    public List<DeptBo> getDeptList(String deptName) {
        List<SysDeptPo> allDepartments = lambdaQuery().eq(SysDeptPo::getDelFlag, 1)
                .like(StringUtils.hasText(deptName), SysDeptPo::getDeptName, deptName)
                .list();
        List<DeptBo> deptList = new ArrayList<>();
        for (SysDeptPo po : allDepartments) {
            deptList.add(DeptBo.po2bo(po));
        }

        //搜索
        if (StringUtils.hasText(deptName)) {
            Map<Long, List<DeptBo>> menuChildrenMap = new HashMap<>();
            List<DeptBo> orphanMenus = new ArrayList<>();
            // 找出每个DeptBo对象的子集
            for (DeptBo dept : deptList) {
                List<DeptBo> children = findChildrenRecursive(deptList, dept.getDeptId());
                if (!children.isEmpty()) {
                    menuChildrenMap.put(dept.getDeptId(), children);
                }
            }

            // 找出没有关联的菜单项
            for (DeptBo dept : deptList) {
                if (dept.getDeptParentId() != null && !menuChildrenMap.containsKey(dept.getDeptParentId())) {
                    orphanMenus.add(dept);
                }
            }
            List<DeptBo> bos = new ArrayList<>();
            menuChildrenMap.forEach((k, v) -> {
                DeptBo deptBo = deptList.stream().filter(t -> t.getDeptId() == k).collect(Collectors.toList()).get(0);
                deptBo.setDeptParentId(0L);
                bos.add(deptBo);
                bos.addAll(v);
            });

            for (int i = 0; i < orphanMenus.size(); i++) {
                DeptBo orp = orphanMenus.get(i);
                for (DeptBo item : bos) {
                    if (item.getDeptId() == orp.getDeptId()) {
                        orphanMenus.remove(orp);
                    }
                }
            }
            orphanMenus.forEach(k -> k.setDeptParentId(0L));
            bos.addAll(orphanMenus);
            return bos;
        }
        return deptList;
    }

    // 递归查找某个DeptBo及其所有子菜单
    private static List<DeptBo> findChildrenRecursive(List<DeptBo> deptList, Long parentId) {
        List<DeptBo> children = new ArrayList<>();
        for (DeptBo menu : deptList) {
            if (parentId.equals(menu.getDeptParentId())) {
                children.add(menu);
                children.addAll(findChildrenRecursive(deptList, menu.getDeptId()));
            }
        }
        return children;
    }

    @Override
    public DeptBo getDeptDetail(Long deptId) {
        Assert.notNull(deptId, "部门id不能为空!");
        SysDeptPo deptPo = lambdaQuery().eq(SysDeptPo::getDelFlag, 1).eq(SysDeptPo::getDeptId, deptId).one();
        if (null == deptPo || null == deptPo.getDeptId()) {
            throw new BusException("此部门不存在!");
        }
        DeptBo deptBo = DeptBo.po2bo(deptPo);
        //获取父级信息
        if (null != deptPo.getDeptParentId()) {
            SysDeptPo parentDeptPo = lambdaQuery().eq(SysDeptPo::getDelFlag, 1).eq(SysDeptPo::getDeptId, deptPo.getDeptParentId()).one();
            if (null != parentDeptPo && null != parentDeptPo.getDeptId()) {
                deptBo.setDeptParentName(parentDeptPo.getDeptName());
            }
        }
        return deptBo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addMenu(DeptBo deptBo) {
        deptBo.setDeptId(null);
        if (null == deptBo.getDeptParentId()) {
            deptBo.setDeptParentId(1L);
        }

        Assert.hasText(deptBo.getDeptCode(), "部门编码不能为空!");
        Long count = lambdaQuery().eq(SysDeptPo::getDelFlag, 1).eq(SysDeptPo::getDeptCode, deptBo.getDeptCode()).count();
        if (count > 0) {
            throw new BusException("此部门编码已经存在!");
        }
        Assert.hasText(deptBo.getDeptName(), "部门名称不能为空!");
        Assert.notNull(deptBo.getDeptStatus(), "部门状态不能为空!");
        if (1 != deptBo.getDeptStatus() && 0 != deptBo.getDeptStatus()) {
            throw new BusException("部门状态格式不正确! 1正常 0停用");
        }
        deptBo.setChildren(null);

        SysDeptPo sysDeptPo = DeptBo.bo2po(deptBo);
        sysDeptPo.setCreateTime(LocalDateTime.now());
        sysDeptPo.setCreateBy(UserContextHolder.getUser().getUserName());
        sysDeptPo.setDelFlag(1);
        return save(sysDeptPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean editDept(DeptBo deptBo) {
        Assert.notNull(deptBo.getDeptId(), "部门id不能为空!");
        if (null == deptBo.getDeptParentId()) {
            deptBo.setDeptParentId(1L);
        }
        Assert.hasText(deptBo.getDeptCode(), "部门编码不能为空!");
        Long count = lambdaQuery().eq(SysDeptPo::getDelFlag, 1).eq(SysDeptPo::getDeptCode, deptBo.getDeptCode())
                .ne(SysDeptPo::getDeptId, deptBo.getDeptId())
                .count();

        if (count > 0) {
            throw new BusException("修改失败,此部门编码已经存在!");
        }
        Assert.hasText(deptBo.getDeptName(), "部门名称不能为空!");
        Assert.notNull(deptBo.getDeptStatus(), "部门状态不能为空!");
        if (1 != deptBo.getDeptStatus() && 0 != deptBo.getDeptStatus()) {
            throw new BusException("部门状态格式不正确! 1正常 0停用");
        }

        deptBo.setChildren(null);
        SysDeptPo sysDeptPo = DeptBo.bo2po(deptBo);
        sysDeptPo.setUpdateTime(LocalDateTime.now());
        sysDeptPo.setUpdateBy(UserContextHolder.getUser().getUserName());
        return updateById(sysDeptPo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDept(Long deptId) {
        Assert.notNull(deptId, "部门id不能为空!");
        Long count = lambdaQuery().eq(SysDeptPo::getDelFlag, 1).eq(SysDeptPo::getDeptParentId, deptId).count();
        if (count > 0) {
            throw new BusException("此部门存在子集,请先删除子集!");
        }
        return lambdaUpdate().eq(SysDeptPo::getDeptId, deptId).set(SysDeptPo::getUpdateBy, UserContextHolder.getUser().getUserName())
                .set(SysDeptPo::getUpdateTime, LocalDateTime.now())
                .set(SysDeptPo::getDelFlag, 0).update();
    }
}
