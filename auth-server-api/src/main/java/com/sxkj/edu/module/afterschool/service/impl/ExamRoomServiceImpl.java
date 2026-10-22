package com.sxkj.edu.module.afterschool.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomAddAO;
import com.sxkj.edu.module.afterschool.domain.dto.ExamRoomQueryAO;
import com.sxkj.edu.module.afterschool.domain.entity.ExamRoomEntity;
import com.sxkj.edu.module.afterschool.domain.excel.ExamRoomExcel;
import com.sxkj.edu.module.afterschool.domain.excel.ExamRoomExportExcel;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.service.SchoolManageService;
import com.sxkj.edu.module.afterschool.domain.vo.ExamRoomVO;
import com.sxkj.edu.module.afterschool.mapper.ExamRoomMapper;
import com.sxkj.edu.module.afterschool.service.ExamRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 考场信息 Service 实现类
 *
 * @author system
 * @date 2026-05-25
 */
@Service
@Slf4j
public class ExamRoomServiceImpl extends ServiceImpl<ExamRoomMapper, ExamRoomEntity> implements ExamRoomService {

    @Resource
    private SchoolManageService schoolManageService;

    // ======================== 通用私有方法 ========================

    /**
     * 校验该学校同一位置教室名称是否已存在
     */
    private boolean existsByNameAndAddress(String roomName, String roomAddress, Long schoolId, Long excludeId) {
        LambdaQueryWrapper<ExamRoomEntity> w = new LambdaQueryWrapper<>();
        w.eq(ExamRoomEntity::getIsValid, 1)
         .eq(ExamRoomEntity::getSchoolId, schoolId)
         .eq(ExamRoomEntity::getRoomAddress, roomAddress)
         .eq(ExamRoomEntity::getRoomName, roomName);
        if (excludeId != null) {
            w.ne(ExamRoomEntity::getId, excludeId);
        }
        return this.count(w) > 0;
    }

    /**
     * Entity -> VO
     */
    private ExamRoomVO toVO(ExamRoomEntity entity) {
        if (entity == null) return null;
        ExamRoomVO vo = new ExamRoomVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    // ======================== 对外方法 ========================

    @Override
    public PageResultDTO<ExamRoomVO> queryExamRoomPage(ExamRoomQueryAO qo) {
        Page<ExamRoomVO> page = baseMapper.pageExamRoomList(new Page<>(qo.getPageIndex(), qo.getPageSize()), qo);
        PageResultDTO<ExamRoomVO> result = new PageResultDTO<>();
        result.setTotal(page.getTotal());
        result.setPages(page.getPages());
        result.setPageIndex(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setList(page.getRecords());
        return result;
    }

    @Override
    public JsonResult<String> addExamRoom(ExamRoomAddAO ao) {
        // 唯一校验
        if (existsByNameAndAddress(ao.getRoomName(), ao.getRoomAddress(), ao.getSchoolId(), null)) {
            return JsonResult.buildError("该校同一楼层教室名称已存在");
        }
        
        ExamRoomEntity entity = new ExamRoomEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setIsValid(1);
        entity.setCreateTime(new Date());
        entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean save = this.save(entity);
        if (!save) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> updateExamRoom(ExamRoomAddAO ao) {
        if (ao.getId() == null) return JsonResult.buildError("教室ID不能为空");
        ExamRoomEntity exist = this.getById(ao.getId());
        if (exist == null || exist.getIsValid() != 1) return JsonResult.buildError("教室记录不存在");
        
        // 唯一校验
        if (existsByNameAndAddress(ao.getRoomName(), ao.getRoomAddress(), ao.getSchoolId(), ao.getId())) {
            return JsonResult.buildError("该校同一楼层教室名称已存在");
        }
        
        ExamRoomEntity entity = new ExamRoomEntity();
        BeanUtils.copyProperties(ao, entity);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<String> deleteExamRoom(Long id) {
        ExamRoomEntity entity = this.getById(id);
        if (entity == null || entity.getIsValid() != 1) return JsonResult.buildError("考场记录不存在");
        entity.setIsValid(0);
        entity.setUpdateTime(new Date());
        entity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        boolean flag = this.updateById(entity);
        if (!flag) return JsonResult.buildError("操作失败");
        return JsonResult.buildSuccess("操作成功");
    }

    @Override
    public JsonResult<ExamRoomVO> getExamRoomDetail(Long id) {
        ExamRoomVO vo = baseMapper.selectExamRoomDetail(id);
        if (vo == null) return JsonResult.buildError("考场记录不存在");
        return JsonResult.buildSuccess(vo);
    }

    @Override
    public JsonResult<String> importExamRoom(Long schoolId, MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            List<ExamRoomExcel> list = ExcelUtils.customerReadExcel(is, ExamRoomExcel.class,2);
            if (list == null || list.isEmpty()) return JsonResult.buildError("导入数据为空");

            // 预加载所有学校名称->ID映射
            List<SchoolManagePo> allSchools = schoolManageService.list(
                    new LambdaQueryWrapper<SchoolManagePo>().eq(SchoolManagePo::getIsValid, 1));
            Map<String, Long> schoolNameIdMap = allSchools.stream()
                    .collect(Collectors.toMap(SchoolManagePo::getSchoolName, SchoolManagePo::getId, (v1, v2) -> v1));
            
            List<String> errors = new ArrayList<>();
            int row = 3;
            for (ExamRoomExcel excel : list) {
                if (StrUtil.isBlank(excel.getRoomName())) {
                    errors.add("第" + row + "行: 教室名不能为空");
                    row++;
                    continue;
                }

                // 通过学校名称查找学校ID
                Long resolvedSchoolId = schoolId;
                if (StrUtil.isNotBlank(excel.getSchoolName())) {
                    resolvedSchoolId = schoolNameIdMap.get(excel.getSchoolName());
                    if (resolvedSchoolId == null) {
                        errors.add("第" + row + "行: 学校名称[" + excel.getSchoolName() + "]不存在，跳过");
                        row++;
                        continue;
                    }
                }
                
                // 唯一校验
                if (existsByNameAndAddress(excel.getRoomName(), excel.getRoomAddress(), resolvedSchoolId, null)) {
                    errors.add("第" + row + "行: 教室名[" + excel.getRoomName() + "]在[" + excel.getRoomAddress() + "]已存在，跳过");
                    row++;
                    continue;
                }
                
                ExamRoomEntity entity = new ExamRoomEntity();
                entity.setSchoolId(resolvedSchoolId);
                entity.setRoomName(excel.getRoomName());
                entity.setRoomAddress(excel.getRoomAddress());
                entity.setRoomNumber(excel.getRoomNumber());
                entity.setRemark(excel.getRemark());
                entity.setIsValid(1);
                entity.setCreateTime(new Date());
                entity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
                this.save(entity);
                row++;
            }
            
            if (!errors.isEmpty()) {
                return JsonResult.buildError("部分数据导入失败：" + String.join("；", errors));
            }
            return JsonResult.buildSuccess("导入成功");
        } catch (Exception e) {
            log.error("考场导入异常", e);
            return JsonResult.buildError("导入异常：" + e.getMessage());
        }
    }

    @Override
    public void exportExamRoom(ExamRoomQueryAO qo, HttpServletResponse response) {

        PageResultDTO<ExamRoomVO> pageResult = this.queryExamRoomPage(qo);
        List<ExamRoomVO> list = pageResult.getList();
        
        if (list == null || list.isEmpty()) {
            return;
        }
        
        List<ExamRoomExportExcel> excelList = list.stream().map(e -> {
            ExamRoomExportExcel ex = new ExamRoomExportExcel();
            BeanUtils.copyProperties(e, ex);
            ex.setSequence(String.valueOf(list.indexOf(e) + 1));
            return ex;
        }).collect(Collectors.toList());
        
        try {
            ExcelUtils.exportExcel(response, excelList, ExamRoomExportExcel.class, "教室管理导出", "教室管理");
        } catch (Exception e) {
            log.error("考场导出异常", e);
        }
    }

    @Override
    public String resolvePlaceName(String rawPlaceName) {
        if (rawPlaceName == null || rawPlaceName.trim().isEmpty()) {
            return rawPlaceName;
        }
        String[] ids = rawPlaceName.split(",");
        List<String> names = new ArrayList<>();
        for (String idStr : ids) {
            String trimmed = idStr.trim();
            try {
                Long roomId = Long.parseLong(trimmed);
                ExamRoomEntity room = baseMapper.selectById(roomId);
                if (room != null) {
                    StringBuilder sb = new StringBuilder();
                    if (room.getRoomAddress() != null && !room.getRoomAddress().isEmpty()) {
                        sb.append(room.getRoomAddress());
                    }
                    if (room.getRoomNumber() != null && !room.getRoomNumber().isEmpty()) {
                        if (sb.length() > 0) sb.append(" ");
                        sb.append(room.getRoomNumber());
                    }
                    if (room.getRoomName() != null && !room.getRoomName().isEmpty()) {
                        if (sb.length() > 0) sb.append(" ");
                        sb.append(room.getRoomName());
                    }
                    names.add(sb.length() > 0 ? sb.toString() : trimmed);
                } else {
                    names.add(trimmed);
                }
            } catch (NumberFormatException e) {
                names.add(trimmed);
            }
        }
        return String.join(",", names);
    }

    /**
     * 批量解析教室名称（高性能版本）。
     * 实现思路：
     * 1. 收集所有placeName字符串中的教室ID（去重）
     * 2. 一次性批量查询所有教室信息
     * 3. 构建ID->名称映射，在内存中完成所有替换
     *
     * @param rawPlaceNames 原始教室ID字符串集合（逗号分隔，如"1,2"或"3"）
     * @return key=原始placeName字符串，value=解析后的可读名称
     */
    @Override
    public Map<String, String> batchResolvePlaceNames(Collection<String> rawPlaceNames) {
        if (rawPlaceNames == null || rawPlaceNames.isEmpty()) {
            return new HashMap<>();
        }
        // Step 1: 收集所有教室ID并去重
        Set<Long> allRoomIds = new HashSet<>();
        for (String placeName : rawPlaceNames) {
            if (placeName == null || placeName.trim().isEmpty()) {
                continue;
            }
            for (String idStr : placeName.split(",")) {
                String trimmed = idStr.trim();
                try {
                    allRoomIds.add(Long.parseLong(trimmed));
                } catch (NumberFormatException ignored) {
                    // 非数字ID，忽略
                }
            }
        }
        if (allRoomIds.isEmpty()) {
            return new HashMap<>();
        }
        // Step 2: 一次性批量查询所有教室，构建ID -> 可读名称映射
        Map<Long, String> roomIdNameMap = new HashMap<>();
        List<ExamRoomEntity> rooms = baseMapper.selectBatchIds(allRoomIds);
        if (rooms != null) {
            for (ExamRoomEntity room : rooms) {
                StringBuilder sb = new StringBuilder();
                if (room.getRoomAddress() != null && !room.getRoomAddress().isEmpty()) {
                    sb.append(room.getRoomAddress());
                }
                if (room.getRoomNumber() != null && !room.getRoomNumber().isEmpty()) {
                    if (sb.length() > 0) sb.append(" ");
                    sb.append(room.getRoomNumber());
                }
                if (room.getRoomName() != null && !room.getRoomName().isEmpty()) {
                    if (sb.length() > 0) sb.append(" ");
                    sb.append(room.getRoomName());
                }
                roomIdNameMap.put(room.getId(), sb.length() > 0 ? sb.toString() : String.valueOf(room.getId()));
            }
        }
        // Step 3: 对每个原始placeName，用映射表替换ID为名称（无数据库请求）
        Map<String, String> resultMap = new HashMap<>();
        for (String placeName : rawPlaceNames) {
            if (placeName == null || placeName.trim().isEmpty()) {
                continue;
            }
            String[] ids = placeName.split(",");
            List<String> names = new ArrayList<>();
            for (String idStr : ids) {
                String trimmed = idStr.trim();
                try {
                    Long roomId = Long.parseLong(trimmed);
                    String resolved = roomIdNameMap.get(roomId);
                    names.add(resolved != null ? resolved : trimmed);
                } catch (NumberFormatException e) {
                    names.add(trimmed);
                }
            }
            resultMap.put(placeName, String.join(",", names));
        }
        return resultMap;
    }
}
