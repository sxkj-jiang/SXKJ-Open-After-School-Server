package com.sxkj.edu.module.mapper.base.statistics;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.PlateOperatePvStatisticsAo;
import com.sxkj.edu.domain.sys.entity.mult.StatisticsPv;
import com.sxkj.edu.domain.vo.PlateOperatePvStatisticsVo;
import org.apache.ibatis.annotations.Param;

public interface StatisticsPvMapper extends BaseMapper<StatisticsPv> {

    Page<PlateOperatePvStatisticsVo> getPlateOperatePvStatistics(Page<PlateOperatePvStatisticsVo> page, @Param("query") PlateOperatePvStatisticsAo ao);
}
