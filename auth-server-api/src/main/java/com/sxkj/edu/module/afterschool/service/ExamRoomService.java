package com.sxkj.edu.module.afterschool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.ExamRoomEntity;
import com.sxkj.edu.module.afterschool.domain.vo.ExamRoomVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 考场信息 Service 接口
 *
 * @author system
 * @date 2026-05-25
 */
public interface ExamRoomService extends IService<ExamRoomEntity> {

    /**
     * 分页查询考场列表
     */
    PageResultDTO<ExamRoomVO> queryExamRoomPage(ExamRoomQueryAO qo);

    /**
     * 新增考场
     */
    JsonResult<String> addExamRoom(ExamRoomAddAO ao);

    /**
     * 编辑考场
     */
    JsonResult<String> updateExamRoom(ExamRoomAddAO ao);

    /**
     * 删除考场(逻辑删除)
     */
    JsonResult<String> deleteExamRoom(Long id);

    /**
     * 查询考场详情
     */
    JsonResult<ExamRoomVO> getExamRoomDetail(Long id);

    /**
     * 导入考场数据
     */
    JsonResult<String> importExamRoom(Long schoolId, MultipartFile file);

    /**
     * 导出考场数据
     */
    void exportExamRoom(ExamRoomQueryAO qo, HttpServletResponse response);

    /**
     * 将教室ID（多个用逗号分隔）解析为可读的教室名称。
     * 格式：roomAddress + roomNumber + roomName，如"综合楼 二楼 203"
     */
    String resolvePlaceName(String rawPlaceName);

    /**
     * 批量解析教室名称（高性能版本）。
     * 将所有记录的教室ID一次性查询并缓存，避免循环内逐条查询。
     *
     * @param rawPlaceNames 原始教室ID字符串集合（逗号分隔）
     * @return rawPlaceName -> 解析后的教室名称
     */
    Map<String, String> batchResolvePlaceNames(java.util.Collection<String> rawPlaceNames);
}
