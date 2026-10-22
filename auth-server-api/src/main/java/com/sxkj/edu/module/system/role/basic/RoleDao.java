package com.sxkj.edu.module.system.role.basic;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.module.system.role.basic.domain.dto.RolePageVO;
import com.sxkj.edu.module.system.role.basic.domain.entity.RoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface RoleDao extends BaseMapper<RoleEntity> {


    RoleEntity getByRoleName(@Param("roleName") String name);

    Page<RolePageVO> rolePage(Page<RolePageVO> page, @Param("roleName") String roleName, @Param("userIds") List<Long> userIds);

}
