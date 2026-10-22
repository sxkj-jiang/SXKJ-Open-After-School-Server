package com.sxkj.edu.module.system.department;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.common.base.domain.PageResultDTO;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.ListPageUtils;
import com.sxkj.edu.common.page.SmartPageUtil;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.module.system.department.domain.dto.DepartmentCreateDTO;
import com.sxkj.edu.module.system.department.domain.dto.DepartmentUpdateDTO;
import com.sxkj.edu.module.system.department.domain.dto.DepartmentVO;
import com.sxkj.edu.module.system.department.domain.entity.DepartmentEntity;
import com.sxkj.edu.module.system.employee.TBaseUserDao;
import com.sxkj.edu.module.system.employee.domain.ao.UserDeptSearchAo;
import com.sxkj.edu.module.system.employee.domain.ao.UserQueryAO;
import com.sxkj.edu.module.system.employee.domain.dto.EmployeeDTO;
import com.sxkj.edu.module.system.role.basic.domain.dto.RoleSelectedVO;
import com.sxkj.edu.module.system.role.roleemployee.RoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 部门管理业务类
 *
 * @author listen
 * @date 2017/12/19 14:25
 */
@Service
@Slf4j
public class DepartmentService extends ServiceImpl<DepartmentDao, DepartmentEntity> {

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private TBaseUserDao employeeDao;

    @Autowired
    private DepartmentTreeService departmentTreeService;

    @Autowired
    private RoleUserService roleUserService;

    /**
     * 获取部门树形结构
     *
     * @return
     */
    public JsonResult<List<DepartmentVO>> listDepartment() {
        UserDeptSearchAo deptSearchAo = new UserDeptSearchAo();
        List<DepartmentVO> departmentVOList = departmentDao.listAll(deptSearchAo);
        List<DepartmentVO> result = departmentTreeService.buildTree(departmentVOList);
        return JsonResult.buildSuccess(result);
    }

    /**
     * 获取所有部门和员工信息
     *
     * @param searchAo
     * @return
     */
    public JsonResult<PageResultDTO<DepartmentVO>> listAllDepartmentEmployee(UserDeptSearchAo searchAo) {
        if (searchAo == null) searchAo = new UserDeptSearchAo();
        Page pageParam = SmartPageUtil.convert2QueryPage(searchAo);
        // 获取全部部门列表
        List<DepartmentVO> departmentVOList = departmentDao.listAll(searchAo);
        if (StringUtils.isNotBlank(searchAo.getDeptName())) {
            // 检索条件不为空的时候 过滤部门列表
            departmentVOList = filterDepartment(departmentVOList, searchAo.getDeptName());
        }

        Map<Long, DepartmentVO> departmentMap = departmentVOList.stream().collect(Collectors.toMap(DepartmentVO::getId, Function.identity()));
        // 获取员工列表
        UserQueryAO userDeptSearchAo = new UserQueryAO();
        userDeptSearchAo.setKeyword(searchAo.getKeyWord());
        List<EmployeeDTO> employeeList = employeeDao.selectEmployeeAllList(userDeptSearchAo);
        employeeList.forEach(employeeDTO -> {

            DepartmentVO departmentVO = departmentMap.get(employeeDTO.getDepartmentId());
            if (null == departmentVO) {
                return;
            }
            List<EmployeeDTO> employeeDTOList = departmentVO.getEmployees();
            if (null == employeeDTOList) {
                employeeDTOList = new ArrayList<>();
            }
            // 根据用户获取用户角色集合
            JsonResult<List<RoleSelectedVO>> listJsonResult = roleUserService.getRolesByEmployeeId(employeeDTO.getId());
            if (null != listJsonResult && CollectionUtils.isNotEmpty(listJsonResult.getData())) {
                employeeDTO.setUserRoleList(listJsonResult.getData());
            } else {
                employeeDTO.setUserRoleList(new ArrayList<>());
            }
            employeeDTOList.add(employeeDTO);
            departmentVO.setEmployees(employeeDTOList);
        });
        List<DepartmentVO> result = departmentTreeService.buildTree(departmentVOList);
        ListPageUtils<DepartmentVO> listSub = new ListPageUtils(pageParam.getCurrent(), pageParam.getSize(), result.size());
        // 手动分页
        List<DepartmentVO> subArrList = listSub.setListSplitForPage(result, listSub);
        PageResultDTO pageResultDTO = new PageResultDTO<>();
        SmartBeanUtil.copyProperties(listSub, pageResultDTO);
        pageResultDTO.setList(subArrList);

        return JsonResult.buildSuccess(pageResultDTO);
    }

    /**
     * 分页查询部门员工
     *
     * @param searchAo
     * @return
     */
//    public JsonResult<PageResultDTO<EmployeeDTO>> queryDeptUserPage(UserDeptSearchAo searchAo) {
//        if(searchAo == null) searchAo = new UserDeptSearchAo();
//        Page pageParam = SmartPageUtil.convert2QueryPage(searchAo);
//
//        // 获取员工列表
//        UserQueryAO userDeptSearchAo = new UserQueryAO();
//        userDeptSearchAo.setDeptName(searchAo.getDeptName());
//        userDeptSearchAo.setKeyword(searchAo.getKeyWord());
//
//        List<EmployeeDTO> employeeList = employeeDao.selectEmployeeAllList(userDeptSearchAo);
//        List<EmployeeDTO> resList = new ArrayList<>();
//        employeeList.forEach(employeeDTO -> {
//
//            // 根据用户获取用户角色集合
//            JsonResult<List<RoleSelectedVO>> listJsonResult = roleUserService.getRolesByEmployeeId(employeeDTO.getId());
//            if(null != listJsonResult && CollectionUtils.isNotEmpty(listJsonResult.getData())){
//                employeeDTO.setUserRoleList(listJsonResult.getData());
//            }else{
//                employeeDTO.setUserRoleList(new ArrayList<>());
//            }
//            resList.add(employeeDTO);
//        });
//        ListPageUtils<EmployeeDTO> listSub = new ListPageUtils(pageParam.getCurrent(),pageParam.getSize(),resList.size());
//        // 手动分页
//        List<EmployeeDTO> subArrList = listSub.setListSplitForPage(resList,listSub);
//        PageResultDTO pageResultDTO = new PageResultDTO<>();
//        SmartBeanUtil.copyProperties(listSub,pageResultDTO);
//        pageResultDTO.setList(subArrList);
//
//        return JsonResult.buildSuccess(pageResultDTO);
//    }

    /**
     * 过滤部门名称，获取过滤后的结果
     *
     * @author lidoudou
     * @date 2019/4/28 20:17
     */
    private List<DepartmentVO> filterDepartment(List<DepartmentVO> departmentVOList, String departmentName) {
        Map<Long, DepartmentVO> departmentMap = new HashMap<>();
        departmentVOList.forEach(item -> {
            if (item.getName().indexOf(departmentName) < 0) {
                return;
            }
            // 当前部门包含关键字
            departmentMap.put(item.getId(), item);
            Long parentId = item.getParentId();
            if (null != parentId) {
                List<DepartmentVO> filterResult = new ArrayList<>();
                getParentDepartment(departmentVOList, parentId, filterResult);
                for (DepartmentVO dto : filterResult) {
                    if (!departmentMap.containsKey(dto.getId())) {
                        departmentMap.put(dto.getId(), dto);
                    }
                }
            }
        });
        return departmentMap.values().stream().collect(Collectors.toList());
    }

    private List<DepartmentVO> getParentDepartment(List<DepartmentVO> departmentVOList, Long parentId, List<DepartmentVO> result) {
        List<DepartmentVO> deptList = departmentVOList.stream().filter(e -> e.getId().equals(parentId)).collect(Collectors.toList());
        for (DepartmentVO item : deptList) {
            result.add(item);
            if (item.getParentId() != null && item.getParentId() != 0) {
                result.addAll(getParentDepartment(departmentVOList, item.getParentId(), result));
            }
        }
        return result;
    }

    /**
     * 新增添加部门
     *
     * @param departmentCreateDTO
     * @return AjaxResult
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> addDepartment(DepartmentCreateDTO departmentCreateDTO) {
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(departmentCreateDTO, DepartmentEntity.class);
        List<DepartmentEntity> existList = getDeptByNameExist(departmentCreateDTO.getName());
        if (CollectionUtils.isNotEmpty(existList)) {
            return JsonResult.buildError(ResultCode.DATA_EXIST.code(), "当前部门名称已存在");
        }
        departmentEntity.setCreateTime(new Date());
        departmentEntity.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        departmentDao.insert(departmentEntity);
        return JsonResult.buildSuccess();
    }

    /**
     * 更新部门信息
     *
     * @param updateDTO
     * @return AjaxResult<String>
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> updateDepartment(DepartmentUpdateDTO updateDTO) {
        if (updateDTO.getId() == null) {
            return JsonResult.buildError(ResultCode.DEPT_ID_ERROR.code(), ResultCode.getMapByCode(ResultCode.DEPT_ID_ERROR.code()));
        }
        DepartmentEntity entity = departmentDao.selectById(updateDTO.getId());
        if (entity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.NOT_EXISTS.code()));
        }
        List<DepartmentEntity> deptList = getDeptByNameIdExist(updateDTO.getName(), updateDTO.getId());
        if (CollectionUtils.isNotEmpty(deptList) && deptList.size() > 0) {
            return JsonResult.buildError(ResultCode.DATA_EXIST.code(), "当前部门名称已存在，请重新修改");
        }
        DepartmentEntity departmentEntity = SmartBeanUtil.copy(updateDTO, DepartmentEntity.class);
        if (!CheckParamUtil.checkIntegerStatusParam(updateDTO.getSort())) {
            departmentEntity.setSort(entity.getSort());
        }
        departmentEntity.setUpdateTime(new Date());
        departmentEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        departmentDao.updateById(departmentEntity);
        return JsonResult.buildSuccess();
    }

    /**
     * 根据id删除部门
     * 1、需要判断当前部门是否有子部门,有子部门则不允许删除
     * 2、需要判断当前部门是否有员工，有员工则不能删除
     *
     * @param deptId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> delDepartment(Long deptId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(deptId);
        if (null == departmentEntity) {
            return JsonResult.buildError(ResultCode.DEPT_NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.DEPT_NOT_EXISTS.code()));
        }
        // 是否有子级部门
        int subDepartmentNum = departmentDao.countSubDepartment(deptId);
        if (subDepartmentNum > 0) {
            return JsonResult.buildError(ResultCode.CANNOT_DEL_DEPARTMENT_WITH_CHILD.code(), ResultCode.getMapByCode(ResultCode.CANNOT_DEL_DEPARTMENT_WITH_CHILD.code()));
        }

        // 是否有未删除员工
        int employeeNum = employeeDao.countByDepartmentId(deptId, true);
        if (employeeNum > 0) {
            return JsonResult.buildError(ResultCode.CANNOT_DEL_DEPARTMENT_WITH_EMPLOYEE.code(), ResultCode.getMapByCode(ResultCode.CANNOT_DEL_DEPARTMENT_WITH_EMPLOYEE.code()));
        }
        deleteDeptById(deptId);
        return JsonResult.buildSuccess();
    }


    /**
     * 根据id获取部门信息
     *
     * @param departmentId
     * @return AjaxResult<DepartmentVO>
     */
    public JsonResult<DepartmentVO> getDepartmentById(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return JsonResult.buildError(ResultCode.DEPT_NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.DEPT_NOT_EXISTS.code()));
        }
        DepartmentVO departmentVO = SmartBeanUtil.copy(departmentEntity, DepartmentVO.class);
        return JsonResult.buildSuccess(departmentVO);
    }

    /**
     * 获取所有部门
     *
     * @return
     */
    public JsonResult<List<DepartmentVO>> listAll() {
        UserDeptSearchAo deptSearchAo = new UserDeptSearchAo();
        List<DepartmentVO> departmentVOList = departmentDao.listAll(deptSearchAo);
        return JsonResult.buildSuccess(departmentVOList);
    }

    /**
     * 上下移动
     *
     * @param departmentId
     * @param swapId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> upOrDown(Long departmentId, Long swapId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.NOT_EXISTS.code()));
        }
        DepartmentEntity swapEntity = departmentDao.selectById(swapId);
        if (swapEntity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.NOT_EXISTS.code()));
        }
        Integer departmentSort = departmentEntity.getSort();
        departmentEntity.setSort(swapEntity.getSort());
        departmentEntity.setUpdateTime(new Date());
        departmentEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        departmentDao.updateById(departmentEntity);

        swapEntity.setSort(departmentSort);
        swapEntity.setUpdateTime(new Date());
        swapEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        departmentDao.updateById(swapEntity);
        return JsonResult.buildSuccess();
    }

    /**
     * 部门升级
     *
     * @param departmentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> upgrade(Long departmentId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.NOT_EXISTS.code()));
        }
        if (departmentEntity.getParentId() == null || departmentEntity.getParentId().equals(0)) {
            return JsonResult.buildError(ResultCode.ERROR_PARAM.code(), ResultCode.getMapByCode(ResultCode.ERROR_PARAM.code()));
        }
        DepartmentEntity parentEntity = departmentDao.selectById(departmentEntity.getParentId());

        departmentEntity.setParentId(parentEntity.getParentId());
        departmentEntity.setUpdateTime(new Date());
        departmentEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        departmentDao.updateById(departmentEntity);
        return JsonResult.buildSuccess();
    }

    /**
     * 部门降级
     *
     * @param departmentId
     * @param preId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<String> downgrade(Long departmentId, Long preId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.NOT_EXISTS.code()));
        }
        DepartmentEntity preEntity = departmentDao.selectById(preId);
        if (preEntity == null) {
            return JsonResult.buildError(ResultCode.NOT_EXISTS.code(), ResultCode.getMapByCode(ResultCode.NOT_EXISTS.code()));
        }
        departmentEntity.setParentId(preEntity.getId());
        departmentEntity.setUpdateTime(new Date());
        departmentEntity.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        departmentDao.updateById(departmentEntity);
        return JsonResult.buildSuccess();
    }

    /**
     * 逻辑删除部门
     *
     * @param id
     */
    public void deleteDeptById(Long id) {
        UpdateWrapper<DepartmentEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.set("is_valid", 0);
        updateWrapper.set("update_time", new Date());
        updateWrapper.set("update_user", SmartRequestTokenUtil.getRequestUserId());
        boolean uptRow = this.update(updateWrapper);
        log.info("call DepartmentService deleteDeptById in exec result:{}", uptRow);
    }

    private List<DepartmentEntity> getDeptByNameExist(String deptName) {
        List<DepartmentEntity> list = new ArrayList<>();
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepartmentEntity::getName, deptName);
        queryWrapper.eq(DepartmentEntity::getIsValid, 1);
        list = this.list(queryWrapper);
        return list;
    }

    private List<DepartmentEntity> getDeptByNameIdExist(String deptName, Long deptId) {
        List<DepartmentEntity> list = new ArrayList<>();
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepartmentEntity::getName, deptName);
        queryWrapper.ne(DepartmentEntity::getId, deptId);
        queryWrapper.eq(DepartmentEntity::getIsValid, 1);
        list = this.list(queryWrapper);
        return list;
    }

    private boolean checkExistDeptByName(String name) {
        boolean exist = false;
        List<DepartmentEntity> list = getDeptByNameExist(name);
        if (CollectionUtils.isNotEmpty(list) && list.size() > 0) {
            exist = true;
        }
        return exist;
    }

    /**
     * 根据id获取部门名称
     *
     * @param deptId
     * @return
     */
    public String getDeptNameById(Long deptId) {
        String deptName = "";
        if (null == deptId) return deptName;
        DepartmentEntity departmentEntity = this.getById(deptId);
        if (null != departmentEntity) deptName = departmentEntity.getName();
        return deptName;
    }

    /**
     * 根据教职工校友id查询部门
     * @param mateId
     * @return
     */
    public String queryMateDeptNameByMateId(Long mateId){
        String deptName = "";
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(DepartmentEntity::getMateId, mateId);
        queryWrapper.eq(DepartmentEntity::getIsValid, 1);
        List<DepartmentEntity> departmentEntities = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(departmentEntities)){
            deptName = departmentEntities.get(0).getName();
        }
        return deptName;
    }


    public JsonResult<List<DepartmentVO>> listAllDistinct(String name) {
        List<DepartmentVO> departmentVOList = departmentDao.listAllDistinct(name);
        return JsonResult.buildSuccess(departmentVOList);
    }

    /**
     * 教职工校友与部门绑定
     * @param mateId
     * @param deptName
     */
    public void saveOrUpdateMateDept(Long mateId, String deptName) {
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DepartmentEntity::getMateId, mateId);
        queryWrapper.eq(DepartmentEntity::getIsValid, 1);
        queryWrapper.eq(DepartmentEntity::getName, deptName);

        List<DepartmentEntity> list = departmentDao.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(list) && !list.get(0).getName().equals(deptName)){
            DepartmentEntity department = list.get(0);
            department.setName(deptName);
            department.setUpdateTime(new Date());
            department.setUpdateUser(1L);
            departmentDao.updateById(department);
        }else {

            DepartmentEntity departmentEntity = new DepartmentEntity();
            List<DepartmentEntity> depList = baseMapper.selectList(new LambdaQueryWrapper<DepartmentEntity>().orderByDesc(DepartmentEntity::getCode));
            String num = "0";
            if(CollectionUtils.isNotEmpty(depList)){
                String code = depList.get(0).getCode();
                num = String.valueOf(Integer.parseInt(code)+1);
            }
            departmentEntity.setCode(num);
            departmentEntity.setMateId(mateId);
            departmentEntity.setIsValid(1);
            departmentEntity.setName(deptName);
            departmentEntity.setCreateTime(new Date());
            departmentEntity.setCreateUser(1L);
            departmentDao.insert(departmentEntity);
        }
    }

    public void updateByMateId(Long mateId, String deptName) {
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseEntity::getIsValid,1)
                .eq(DepartmentEntity::getMateId,mateId);
        List<DepartmentEntity> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            DepartmentEntity department = list.get(0);
            department.setName(deptName);
            department.setUpdateTime(new Date());
            department.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            this.updateById(department);
        }else {
            DepartmentEntity dep = new DepartmentEntity();
            List<DepartmentEntity> depList = baseMapper.selectList(new LambdaQueryWrapper<DepartmentEntity>().orderByDesc(DepartmentEntity::getCode));
            String num = "0";
            if(CollectionUtils.isNotEmpty(depList)){
                String code = depList.get(0).getCode();
                num = String.valueOf(Integer.parseInt(code)+1);
            }
            dep.setCode(num);
            dep.setMateId(mateId);
            dep.setName(deptName);
            dep.setCreateTime(new Date());
            dep.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
            this.save(dep);
        }
    }

    public String getDeptNameByMateId(Long mateId) {
        String name = "";
        LambdaQueryWrapper<DepartmentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseEntity::getIsValid,1)
                .eq(DepartmentEntity::getMateId,mateId);
        List<DepartmentEntity> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)){
            name = list.get(0).getName();
        }
        return name;
    }
}
