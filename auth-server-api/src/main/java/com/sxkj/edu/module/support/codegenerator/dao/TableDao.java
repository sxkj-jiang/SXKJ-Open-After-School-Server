package com.sxkj.edu.module.support.codegenerator.dao;

import com.sxkj.edu.module.support.codegenerator.domain.ColumnVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface TableDao {


    /**
     * 查询表描述
     * @param tableName
     * @return
     */
    String selectTableDesc(@Param("tableName") String tableName);

    /**
     * 查询表列信息
     * @param tableName
     * @return
     */
    List<ColumnVO> selectTableColumn(@Param("tableName") String tableName);


}
