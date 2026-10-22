package com.sxkj.edu.module.system.privilege.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.module.system.privilege.domain.ao.UserMenuQueryAo;
import com.sxkj.edu.module.system.privilege.domain.entity.UserMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface UserMenuDao extends BaseMapper<UserMenuEntity> {



    /**
     * 菜单管理-根据类型查询
     *
     * @param userMenuQueryAo
     * @return
     */
    List<UserMenuEntity> selectMenuByType(@Param("query") UserMenuQueryAo userMenuQueryAo);

    /**
     * 菜单管理-查询所有一级菜单，树结构
     * @param userMenuQueryAo
     * @return
     */
    List<UserMenuEntity> selectAllMenuByType(@Param("query") UserMenuQueryAo userMenuQueryAo);

    /**
     * 查询所有权限
     *
     * @return
     */
    List<UserMenuEntity> selectAll();



}
