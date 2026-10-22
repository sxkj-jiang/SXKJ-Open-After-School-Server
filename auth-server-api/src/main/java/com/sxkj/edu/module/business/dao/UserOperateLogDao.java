package com.sxkj.edu.module.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.PlateOperateStatisticsQueryAo;
import com.sxkj.edu.domain.sys.dto.UserOperateLogQueryDTO;
import com.sxkj.edu.domain.sys.entity.UserOperateLogEntity;
import com.sxkj.edu.domain.vo.PlateNuserLoginStatisticsVo;
import com.sxkj.edu.domain.vo.PlateOperateFrequencyStatisticsVo;
import com.sxkj.edu.domain.vo.StatisticKeyWordCountVO;
import com.sxkj.edu.domain.vo.StatisticTypeCountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface UserOperateLogDao extends BaseMapper<UserOperateLogEntity> {

    /**
     * 分页查询
     * @param queryDTO
     * @return UserOperateLogEntity
    */
    List<UserOperateLogEntity> queryByPage(Page page, @Param("queryDTO") UserOperateLogQueryDTO queryDTO);

    List<PlateOperateFrequencyStatisticsVo> queryPlateOperateFrequencyList(@Param("query") PlateOperateStatisticsQueryAo ao);

    List<PlateNuserLoginStatisticsVo> queryPlateNuserLoginStatistics(@Param("query")PlateOperateStatisticsQueryAo ao);

    List<StatisticTypeCountVO> queryPlateUserLoginStatistics(@Param("query")PlateOperateStatisticsQueryAo ao);

    List<StatisticKeyWordCountVO> getPlateOperateTime(@Param("query") PlateOperateStatisticsQueryAo ao);

}
