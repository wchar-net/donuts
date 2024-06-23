package net.wchar.donuts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.po.SysDeptPo;

import java.util.List;

/**
 * 部门
 * @author Elijah
 */
public interface SysDeptService extends IService<SysDeptPo> {


    //查询部门列表tree
    DeptBo getDeptTree(Long deptId);

    //查询部门列表
    List<DeptBo> getDeptList(String deptName);

    //添加部门
    Boolean addMenu(DeptBo deptBo);

    //删除部门
    Boolean removeDept(Long deptId);

    //修改部门
    Boolean editDept(DeptBo deptBo);

    //获取部门详情
    DeptBo getDeptDetail(Long deptId);
}
