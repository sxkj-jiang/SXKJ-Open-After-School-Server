package com.sxkj.edu.module.mapper.base.statistics;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.PlateOperatePvStatisticsAo;
import com.sxkj.edu.domain.ao.PlateOperateStatisticsQueryAo;
import com.sxkj.edu.domain.sys.entity.mult.StatisticsEvent;
import com.sxkj.edu.domain.vo.PlateOperateEventStatisticsVo;
import com.sxkj.edu.domain.vo.StatisticKeyWordCountVO;
import com.sxkj.edu.domain.vo.StatisticKeyWordPercentageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface StatisticsEventMapper extends BaseMapper<StatisticsEvent> {

    List<StatisticKeyWordCountVO> getKeyMetricsStatistics(@Param("query") PlateOperateStatisticsQueryAo ao);

    List<StatisticKeyWordCountVO> getHourPvCount(@Param("query")PlateOperateStatisticsQueryAo ao);

    List<StatisticKeyWordCountVO> getHourUvCount(@Param("query")PlateOperateStatisticsQueryAo ao);

    List<StatisticKeyWordCountVO> getDatePvCount(@Param("query")PlateOperateStatisticsQueryAo ao);

    List<StatisticKeyWordCountVO> getDateUvCount(@Param("query")PlateOperateStatisticsQueryAo ao);

    List<StatisticKeyWordPercentageVO> getPlateVisitorShareStatistics();

    List<StatisticKeyWordPercentageVO> getPlateEquipDistributeStatistics(@Param("query")PlateOperateStatisticsQueryAo ao);

    Page<PlateOperateEventStatisticsVo> getPlateOperateEventStatistics(Page<PlateOperateEventStatisticsVo> page, @Param("query")PlateOperatePvStatisticsAo ao);
}
