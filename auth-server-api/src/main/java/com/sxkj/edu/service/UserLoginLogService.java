package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.page.SmartPageUtil;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.webSocket.WebSocketServer;
import com.sxkj.edu.domain.ao.PlateVisitRecordStatisticsPageQueryAo;
import com.sxkj.edu.domain.sys.dto.UserLoginLogDTO;
import com.sxkj.edu.domain.sys.dto.UserLoginLogQueryDTO;
import com.sxkj.edu.domain.sys.entity.UserLoginLogEntity;
import com.sxkj.edu.domain.vo.PlateVisitRecordStatisticsVo;
import com.sxkj.edu.module.business.dao.UserLoginLogDao;
import com.sxkj.edu.module.system.employee.TbaseUserService;
import com.sxkj.edu.module.system.employee.domain.ao.UserQueryAO;
import com.sxkj.edu.module.system.employee.domain.vo.EmployeeVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserLoginLogService {

    @Autowired
    private UserLoginLogDao userLoginLogDao;

    @Autowired
    private TbaseUserService userService;

    /**
     * @author yandanyang
     * @description 分页查询
     * @date 2019-05-15 10:25:21
     */
    public JsonResult<PageResultDTO<UserLoginLogDTO>> queryByPage(UserLoginLogQueryDTO queryDTO) {
        Page page = SmartPageUtil.convert2QueryPage(queryDTO);
        List<UserLoginLogEntity> entities = userLoginLogDao.queryByPage(page, queryDTO);
        List<UserLoginLogDTO> dtoList = SmartBeanUtil.copyList(entities, UserLoginLogDTO.class);
        page.setRecords(dtoList);
        PageResultDTO<UserLoginLogDTO> pageResultDTO = SmartPageUtil.convert2PageResult(page);
        return JsonResult.buildSuccess(pageResultDTO);
    }


    /**
     * 查询员工在线状态
     *
     * @param queryDTO
     * @return
     */
    public JsonResult<PageResultDTO<EmployeeVO>> queryUserOnLine(UserQueryAO queryDTO) {
        List<Long> onLineUserList = WebSocketServer.getOnLineUserList();
        if (CollectionUtils.isEmpty(onLineUserList)) {
            return JsonResult.buildSuccess();
        }
        queryDTO.setUserIds(onLineUserList);
        JsonResult<PageResultDTO<EmployeeVO>> employeeList = userService.selectEmployeeList(queryDTO);
        return employeeList;
    }

    public PageVO<PlateVisitRecordStatisticsVo> queryVisitRecordPage(PlateVisitRecordStatisticsPageQueryAo queryAO) {
        Page<PlateVisitRecordStatisticsVo> page = new Page<>(queryAO.getPageIndex(), queryAO.getPageSize());
        userLoginLogDao.queryVisitRecordPage(page,queryAO);
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }
}
