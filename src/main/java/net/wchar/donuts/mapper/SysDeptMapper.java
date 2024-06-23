package net.wchar.donuts.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.wchar.donuts.model.bo.DeptBo;
import net.wchar.donuts.model.po.SysDeptPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 部门
 * @author Elijah
 */
@Mapper
public interface SysDeptMapper extends BaseMapper<SysDeptPo> {

    //查询部门列表tree 自定义顶级
    DeptBo getDeptTree(@Param("deptId") Long deptId);
}
