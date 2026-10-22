package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.ao.SchoolManageSwithAO;
import com.sxkj.edu.domain.dto.SchoolManageDTO;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.business.entity.SchoolManagePo;
import com.sxkj.edu.module.system.login.domain.LoginStuSchoolDetailVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface SchoolManageService extends IService<SchoolManagePo> {
    PageVO<SchoolManageDTO> pageQuery(SchoolManageDTO SchoolManageDTO, int pageNum, int pageSize);

    JsonResult<String> saveSchoolManage(SchoolManageDTO SchoolManageDTO);

    JsonResult<String> updateSchoolManage(SchoolManageDTO SchoolManageDTO);

    JsonResult<String> deleteSchoolManage(Long id);

    List<SchoolManageDTO> listSchoolManage(SchoolManageDTO SchoolManageDTO);

    LoginStuSchoolDetailVO querySchoolNameById(Long id);

    JsonResult swithSchoolData(SchoolManageSwithAO ao, HttpServletRequest request);

    Map<Long, String> getSchoolNameById(List<Long> schoolIds);

    Map<Long, FileVO> getSchoolLogo(List<Long> schoolIds);

    String getSchoolName(Long id);

    // 获取学校编码:中小学为10数字
    String getSchoolCode(Long id);

    // 校验学校名称是否存在，不存在插入，并返回学校id
    Long registSchoolManageByName(String schoolName);
}
