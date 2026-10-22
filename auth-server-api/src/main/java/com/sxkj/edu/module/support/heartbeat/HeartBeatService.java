package com.sxkj.edu.module.support.heartbeat;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.base.domain.PageParamDTO;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.config.heartbeat.AbstractHeartBeatCommand;
import com.sxkj.edu.common.config.heartbeat.HeartBeatConfig;
import com.sxkj.edu.common.config.heartbeat.HeartBeatLogger;
import com.sxkj.edu.common.config.heartbeat.HeartBeatRecordDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.common.page.SmartPageUtil;
import com.sxkj.edu.config.SmartHeartBeatConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;


@Slf4j
@Service
public class HeartBeatService extends AbstractHeartBeatCommand {

    @Autowired
    private HeartBeatRecordDao heartBeatRecordDao;

    @Autowired
    private SmartHeartBeatConfig heartBeatConfig;

    @PostConstruct
    public void init() {

        HeartBeatConfig config = HeartBeatConfig.builder().delayHandlerTime(heartBeatConfig.getDelayHandlerTime()).intervalTime(heartBeatConfig.getIntervalTime()).build();

        super.init(config, new HeartBeatLogger() {
            @Override
            public void error(String string) {
                log.error(string);
            }

            @Override
            public void error(String string, Throwable e) {
                log.error(string, e);
            }

            @Override
            public void info(String string) {
                log.info(string);
            }
        });
    }

    @PreDestroy
    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void handler(HeartBeatRecordDTO heartBeatRecordDTO) {
        HeartBeatRecordEntity heartBeatRecordEntity = SmartBeanUtil.copy(heartBeatRecordDTO, HeartBeatRecordEntity.class);
        HeartBeatRecordEntity heartBeatRecordOld = heartBeatRecordDao.query(heartBeatRecordEntity);
        if (heartBeatRecordOld == null) {
            heartBeatRecordDao.insertHeartBeat(heartBeatRecordEntity);
        } else {
            heartBeatRecordDao.updateHeartBeatTimeById(heartBeatRecordOld.getId(), heartBeatRecordEntity.getHeartBeatTime());
        }

    }

    public JsonResult<PageResultDTO<HeartBeatRecordVO>> pageQuery(PageParamDTO pageParamDTO) {
        Page pageQueryInfo = SmartPageUtil.convert2QueryPage(pageParamDTO);
        List<HeartBeatRecordVO> recordVOList = heartBeatRecordDao.pageQuery(pageQueryInfo);
        PageResultDTO<HeartBeatRecordVO> pageResultDTO = SmartPageUtil.convert2PageResult(pageQueryInfo, recordVOList);
        return JsonResult.buildSuccess(pageResultDTO);

    }
}
