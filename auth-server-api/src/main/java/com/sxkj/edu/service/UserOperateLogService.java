package com.sxkj.edu.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.SmartPageUtil;
import com.sxkj.edu.common.utils.DateUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.PlateOperateStatisticsQueryAo;
import com.sxkj.edu.domain.sys.dto.UserOperateLogDTO;
import com.sxkj.edu.domain.sys.dto.UserOperateLogQueryDTO;
import com.sxkj.edu.domain.sys.entity.UserOperateLogEntity;
import com.sxkj.edu.domain.vo.*;
import com.sxkj.edu.module.business.dao.UserOperateLogDao;
import com.sxkj.edu.module.mapper.base.statistics.StatisticsEventMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
public class UserOperateLogService {

    @Autowired
    private UserOperateLogDao userOperateLogDao;

    @Autowired
    private StatisticsEventMapper statisticsEventMapper;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-05-15 11:32:14
     */
    public JsonResult<PageResultDTO<UserOperateLogDTO>> queryByPage(UserOperateLogQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<UserOperateLogEntity> entities = userOperateLogDao.queryByPage(page, queryDTO);
        List<UserOperateLogDTO> dtoList = SmartBeanUtil.copyList(entities, UserOperateLogDTO.class);
        page.setRecords(dtoList);
        PageResultDTO<UserOperateLogDTO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return JsonResult.buildSuccess(pageResultDTO);
    }

    /**
     * @author yandanyang
     * @description 添加
     * @date 2019-05-15 11:32:14
     */
    public JsonResult<String> add(UserOperateLogDTO addDTO) {
        UserOperateLogEntity entity = SmartBeanUtil.copy(addDTO, UserOperateLogEntity.class);
        userOperateLogDao.insert(entity);
        return JsonResult.buildSuccess();
    }

    /**
     * @author yandanyang
     * @description 编辑
     * @date 2019-05-15 11:32:14
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> update(UserOperateLogDTO updateDTO) {
        UserOperateLogEntity entity = SmartBeanUtil.copy(updateDTO, UserOperateLogEntity.class);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        userOperateLogDao.updateById(entity);
        return JsonResult.buildSuccess();
    }


    /**
     * @author yandanyang
     * @description 根据ID查询
     * @date 2019-05-15 11:32:14
     */
    public JsonResult<UserOperateLogDTO> detail(Long id) {
        UserOperateLogEntity entity = userOperateLogDao.selectById(id);
        UserOperateLogDTO dto = SmartBeanUtil.copy(entity, UserOperateLogDTO.class);
        return JsonResult.buildSuccess(dto);
    }

    public PlateOperateStatisticsVo getOperateStatistics(PlateOperateStatisticsQueryAo ao) {
        PlateOperateStatisticsVo statisticsAllVo = new PlateOperateStatisticsVo();
        // 关键指标概览统计
        if (ao.getQueryType() == 0 || ao.getQueryType() == 1) {
            statisticsAllVo.setKeyMetricsVo(getKeyMetricsStatistics(ao));
        }
        // 流量趋势统计
        if (ao.getQueryType() == 0 || ao.getQueryType() == 2) {
            statisticsAllVo.setTrafficTrendVoList(getTrafficTrendStatistics(ao));
        }
        // 平台使用时间统计
        if (ao.getQueryType() == 0 || ao.getQueryType() == 3) {
            statisticsAllVo.setTimeTjVoList(getPlateOperateTimeStatistics(ao));
        }
        // 每日登录用户访问量
        if (ao.getQueryType() == 0 || ao.getQueryType() == 4) {
            statisticsAllVo.setLoginUserVoList(getPlateUserLoginStatisticsVo(ao));
        }
        // 平台使用频率
        if (ao.getQueryType() == 0 || ao.getQueryType() == 5) {
            statisticsAllVo.setFrequencyTjVoList(getPlateOperateFrequencyStatistics(ao));
        }
        // 访问端占比统计
        if (ao.getQueryType() == 0 || ao.getQueryType() == 6) {
            statisticsAllVo.setVisitorShareVoList(getPlateVisitorShareStatistics());
        }
        // 设备分布统计
        if (ao.getQueryType() == 0 || ao.getQueryType() == 7) {
            statisticsAllVo.setEquipDistributeVoList(getPlateEquipDistributeStatistics(ao));
        }
        return statisticsAllVo;
    }

    private List<StatisticKeyWordPercentageVO> getPlateEquipDistributeStatistics(PlateOperateStatisticsQueryAo ao) {
        return statisticsEventMapper.getPlateEquipDistributeStatistics(ao);
    }

    private List<StatisticKeyWordPercentageVO> getPlateVisitorShareStatistics() {
       return statisticsEventMapper.getPlateVisitorShareStatistics();
    }

    /**
     * 流量趋势统计
     *
     * @param ao
     * @return
     */
    private List<PlateTrafficTrendStatisticsVo> getTrafficTrendStatistics(PlateOperateStatisticsQueryAo ao) {
        List<PlateTrafficTrendStatisticsVo> voList = new ArrayList<>();
        if (ObjectUtil.isNotNull(ao.getTrafficTrendTimeType()) && 1 == ao.getTrafficTrendTimeType()) {
            //获取小时访问量
            List<StatisticKeyWordCountVO> pvList = statisticsEventMapper.getHourPvCount(ao);
            //获取小时用户数
            List<StatisticKeyWordCountVO> uvList = statisticsEventMapper.getHourUvCount(ao);
            List<String> numberStrings = Arrays.stream(IntStream.rangeClosed(1, 24).toArray())
                    .mapToObj(String::valueOf)
                    .collect(Collectors.toList());
            numberStrings.forEach(e -> {
                PlateTrafficTrendStatisticsVo vo = new PlateTrafficTrendStatisticsVo();
                vo.setXKeyWord(e + "时");
                StatisticKeyWordCountVO pv = pvList.stream().filter(i -> e.equals(i.getKeyword())).findFirst().orElse(null);
                if (null != pv) vo.setPvCount(pv.getCount());

                StatisticKeyWordCountVO uv = uvList.stream().filter(i -> e.equals(i.getKeyword())).findFirst().orElse(null);
                if (null != uv) vo.setUvCount(uv.getCount());
                voList.add(vo);
            });
        } else {
            //获取日期访问量
            List<StatisticKeyWordCountVO> pvList = statisticsEventMapper.getDatePvCount(ao);
            //获取日期用户数
            List<StatisticKeyWordCountVO> uvList = statisticsEventMapper.getDateUvCount(ao);
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            if (ObjectUtil.isNotNull(ao.getTrafficTrendTimeType()) && 2 == ao.getTrafficTrendTimeType()) {
                LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
                for (int i = 0; i < 7; i++) {
                    String formattedDate = startOfWeek.format(formatter);
                    PlateTrafficTrendStatisticsVo vo = new PlateTrafficTrendStatisticsVo();
                    vo.setXKeyWord(formattedDate);
                    StatisticKeyWordCountVO pv = pvList.stream().filter(e -> formattedDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if (null != pv) vo.setPvCount(pv.getCount());

                    StatisticKeyWordCountVO uv = uvList.stream().filter(e -> formattedDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if (null != uv) vo.setUvCount(uv.getCount());
                    voList.add(vo);
                    startOfWeek = startOfWeek.plusDays(1);
                }
            }
            if (ObjectUtil.isNotNull(ao.getTrafficTrendTimeType()) && 3 == ao.getTrafficTrendTimeType()) {
                int year = now.getYear();
                int month = now.getMonthValue();
                for (int day = 1; day <= now.lengthOfMonth(); day++) {
                    LocalDate date = LocalDate.of(year, month, day);
                    String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd"));

                    String diffDate = date.format(formatter);
                    PlateTrafficTrendStatisticsVo vo = new PlateTrafficTrendStatisticsVo();
                    vo.setXKeyWord(formattedDate);

                    StatisticKeyWordCountVO pv = pvList.stream().filter(e -> diffDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if (null != pv) vo.setPvCount(pv.getCount());

                    StatisticKeyWordCountVO uv = uvList.stream().filter(e -> diffDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if (null != uv) vo.setUvCount(uv.getCount());
                    voList.add(vo);
                }
            }
            if (ObjectUtil.isNotNull(ao.getTrafficTrendStartDate()) && ObjectUtil.isNotNull(ao.getTrafficTrendEndDate())) {
                List<Date> betweenDates = DateUtils.getBetweenDates(ao.getTrafficTrendStartDate(), ao.getTrafficTrendEndDate());
                betweenDates.add(0, ao.getTrafficTrendStartDate());
                if (!ao.getTrafficTrendStartDate().equals(ao.getTrafficTrendEndDate()))
                    betweenDates.add(ao.getTrafficTrendEndDate());
                betweenDates.forEach(d -> {
                    PlateTrafficTrendStatisticsVo vo = new PlateTrafficTrendStatisticsVo();
                    String formattedDate = DateUtil.format(d, "yyyy-MM-dd");
                    vo.setXKeyWord(DateUtil.format(d, "yyyy-MM-dd"));

                    StatisticKeyWordCountVO pv = pvList.stream().filter(e -> formattedDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if (null != pv) vo.setPvCount(pv.getCount());

                    StatisticKeyWordCountVO uv = uvList.stream().filter(e -> formattedDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if (null != uv) vo.setUvCount(uv.getCount());
                    voList.add(vo);
                });
            }
        }
        return voList;
    }

    /**
     * 关键指标概览统计
     *
     * @param ao
     * @return
     */
    private KeyMetricsStatisticsVo getKeyMetricsStatistics(PlateOperateStatisticsQueryAo ao) {
        KeyMetricsStatisticsVo statisticsAllVo = new KeyMetricsStatisticsVo();
        List<StatisticKeyWordCountVO> voList = statisticsEventMapper.getKeyMetricsStatistics(ao);
        for (StatisticKeyWordCountVO vo : voList) {
            switch (vo.getKeyword()) {
                case "ymfwl":
                    statisticsAllVo.setPageVisitNum(vo.getCount());
                    break;
                case "dlyhs":
                    statisticsAllVo.setAlongUserNum(vo.getCount());
                    break;
                case "ymsjs":
                    statisticsAllVo.setPageEventNum(vo.getCount());
                    break;
                case "yhdls":
                    statisticsAllVo.setUserLoginNum(vo.getCount());
                    break;
            }
        }
        return statisticsAllVo;
    }

    /**
     * 平台运营-平台使用时间统计
     */
    private List<PlateOperateTimeStatisticsVo> getPlateOperateTimeStatistics(PlateOperateStatisticsQueryAo ao) {
        List<PlateOperateTimeStatisticsVo> voList = new ArrayList<>();

        List<StatisticKeyWordCountVO> list = userOperateLogDao.getPlateOperateTime(ao);
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (ObjectUtil.isNotNull(ao.getUsePlateTimeType()) && 1 == ao.getUsePlateTimeType()) {
            String format = now.format(formatter);
            PlateOperateTimeStatisticsVo vo = new PlateOperateTimeStatisticsVo();
            vo.setTjDate(format);
            if(CollectionUtil.isNotEmpty(list)) vo.setSumHours(list.get(0).getCount());
            voList.add(vo);
        }
        if (ObjectUtil.isNotNull(ao.getUsePlateTimeType()) && 2 == ao.getUsePlateTimeType()) {
            LocalDate startOfWeek = now.minusDays(now.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
            for (int i = 0; i < 7; i++) {
                String formattedDate = startOfWeek.format(formatter);
                PlateOperateTimeStatisticsVo vo = new PlateOperateTimeStatisticsVo();
                vo.setTjDate(formattedDate);

                if(CollectionUtil.isNotEmpty(list)) {
                    StatisticKeyWordCountVO wordCountVO = list.stream().filter(e -> formattedDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if(null != wordCountVO) vo.setSumHours(wordCountVO.getCount());
                }
                voList.add(vo);
                startOfWeek = startOfWeek.plusDays(1);

            }
        }
        if (ObjectUtil.isNotNull(ao.getUsePlateTimeType()) && 3 == ao.getUsePlateTimeType()) {
            int year = now.getYear();
            int month = now.getMonthValue();
            for (int day = 1; day <= now.lengthOfMonth(); day++) {
                LocalDate date = LocalDate.of(year, month, day);
                String formattedDate = date.format(DateTimeFormatter.ofPattern("MM-dd"));

                String diffDate = date.format(formatter);
                PlateOperateTimeStatisticsVo vo = new PlateOperateTimeStatisticsVo();
                vo.setTjDate(formattedDate);

                if(CollectionUtil.isNotEmpty(list)) {
                    StatisticKeyWordCountVO wordCountVO = list.stream().filter(e ->  diffDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if(null != wordCountVO) vo.setSumHours(wordCountVO.getCount());
                }
                voList.add(vo);
            }
        }
        if (ObjectUtil.isNotNull(ao.getUsePlateStartDate()) && ObjectUtil.isNotNull(ao.getUsePlateEndDate())) {
            List<Date> betweenDates = DateUtils.getBetweenDates(ao.getUsePlateStartDate(), ao.getUsePlateEndDate());
            betweenDates.add(0, ao.getUsePlateStartDate());
            if (!ao.getUsePlateStartDate().equals(ao.getUsePlateEndDate()))
                betweenDates.add(ao.getUsePlateEndDate());
            betweenDates.forEach(d -> {
                PlateOperateTimeStatisticsVo vo = new PlateOperateTimeStatisticsVo();
                String formattedDate = DateUtil.format(d, "yyyy-MM-dd");
                vo.setTjDate(DateUtil.format(d, "yyyy-MM-dd"));
                if(CollectionUtil.isNotEmpty(list)) {
                    StatisticKeyWordCountVO wordCountVO = list.stream().filter(e ->formattedDate.equals(e.getKeyword())).findFirst().orElse(null);
                    if(null != wordCountVO) vo.setSumHours(wordCountVO.getCount());
                }
                voList.add(vo);
            });
        }

        return voList;
    }

    /**
     * 平台运营-每日非登录用户访问量
     */
    private List<PlateNuserLoginStatisticsVo> getPlateNuserLoginStatisticsVo(PlateOperateStatisticsQueryAo ao) {
        return userOperateLogDao.queryPlateNuserLoginStatistics(ao);
    }

    /**
     * 平台运营-每日登录用户访问量
     */
    private List<PlateUserLoginStatisticsVo> getPlateUserLoginStatisticsVo(PlateOperateStatisticsQueryAo ao) {
        List<PlateUserLoginStatisticsVo> voList = new ArrayList<>();

        List<StatisticTypeCountVO> list = userOperateLogDao.queryPlateUserLoginStatistics(ao);
        LocalDate currentDate = LocalDate.now();

        if(ObjectUtil.isNotNull(ao.getUserLoginStartDate()) && ObjectUtil.isNotNull(ao.getUserLoginEndDate())) {
            List<Date> betweenDates = DateUtils.getBetweenDates(ao.getUserLoginStartDate(), ao.getUserLoginEndDate());
            betweenDates.add(0, ao.getUserLoginStartDate());
            if (!ao.getUserLoginStartDate().equals(ao.getUserLoginEndDate()))
                betweenDates.add(ao.getUserLoginEndDate());
            betweenDates.forEach(d -> {
                String xDate = DateUtil.format(d, "MM-dd");
                PlateUserLoginStatisticsVo vo = new PlateUserLoginStatisticsVo();
                vo.setDate(xDate);
                if(CollectionUtil.isNotEmpty(list)) {
                    List<StatisticTypeCountVO> countVOS = list.stream().filter(e ->xDate.equals(e.getDate())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(countVOS)) {
                        for (StatisticTypeCountVO countVO : countVOS) {
                            switch (countVO.getType()){
                                case "JWPC":
                                    vo.setGldCount(countVO.getCount());
                                    break;
                                case "PTPC":
                                    vo.setGwdCount(countVO.getCount());
                                    break;
                                case "XCX":
                                    vo.setYddCount(countVO.getCount());
                                    break;
                            }
                        }
                    }
                }
                voList.add(vo);
            });
        }else {
            // //默认展示半个月的数据 从当前日期开始，往前推15天
            for (int i = 0; i <= 15; i++) {
                String xDate = currentDate.minusDays(i).format(DateTimeFormatter.ofPattern("MM-dd"));
                PlateUserLoginStatisticsVo vo = new PlateUserLoginStatisticsVo();
                vo.setDate(xDate);
                if(CollectionUtil.isNotEmpty(list)) {
                    List<StatisticTypeCountVO> countVOS = list.stream().filter(e -> xDate.equals(e.getDate())&& ObjectUtil.isNotNull(e.getType())).collect(Collectors.toList());
                    if(CollectionUtil.isNotEmpty(countVOS)) {
                        for (StatisticTypeCountVO countVO : countVOS) {
                            switch (countVO.getType()){
                                case "JWPC":
                                    vo.setGldCount(countVO.getCount());
                                    break;
                                case "PTPC":
                                    vo.setGwdCount(countVO.getCount());
                                    break;
                                case "XCX":
                                    vo.setYddCount(countVO.getCount());
                                    break;
                            }
                        }
                    }
                }
                voList.add(vo);
            }
        }
        return voList;
    }

    /**
     * 平台运营-平台使用频率统计
     */
    private List<PlateOperateFrequencyStatisticsVo> getPlateOperateFrequencyStatistics(PlateOperateStatisticsQueryAo ao) {
        List<PlateOperateFrequencyStatisticsVo> frequencyVoList = userOperateLogDao.queryPlateOperateFrequencyList(ao);
        return frequencyVoList;
    }
}
