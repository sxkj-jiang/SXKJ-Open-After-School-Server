package com.sxkj.edu.module.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.PlateVisitRecordStatisticsPageQueryAo;
import com.sxkj.edu.domain.sys.dto.UserLoginLogQueryDTO;
import com.sxkj.edu.domain.sys.entity.UserLoginLogEntity;
import com.sxkj.edu.domain.vo.PlateVisitRecordStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Mapper
@Component
public interface UserLoginLogDao extends BaseMapper<UserLoginLogEntity> {

    /**
     * 分页查询
     *
     * @param queryDTO
     * @return UserLoginLogEntity
     */
    List<UserLoginLogEntity> queryByPage(Page page, @Param("queryDTO") UserLoginLogQueryDTO queryDTO);

    Date getLastLoginTime(@Param("id")Long id);

    Page<PlateVisitRecordStatisticsVo> queryVisitRecordPage(Page<PlateVisitRecordStatisticsVo> page,
                                                            @Param("query") PlateVisitRecordStatisticsPageQueryAo queryAO);

}
