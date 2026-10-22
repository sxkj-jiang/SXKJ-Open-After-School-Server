package com.sxkj.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sxkj.edu.common.base.constants.CoreConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.enums.FileTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.common.utils.CheckParamUtil;
import com.sxkj.edu.common.utils.ExcelUtils;
import com.sxkj.edu.common.utils.SmartBeanUtil;
import com.sxkj.edu.common.utils.UUIDUtils;
import com.sxkj.edu.config.AssembleUtil;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import com.sxkj.edu.domain.ao.file.BaseFileUploadAo;
import com.sxkj.edu.domain.base.*;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import com.sxkj.edu.domain.vo.BaseFileUploadVo;
import com.sxkj.edu.domain.vo.FileVO;
import com.sxkj.edu.module.mapper.website.BaseFileMapper;
import com.sxkj.edu.service.BaseFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础-上传附件表 服务实现类
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-30
 */
@Slf4j
@Service
@Transactional
public class BaseFileServiceImpl extends ServiceImpl<BaseFileMapper, BaseFileEntity> implements BaseFileService {

    @Resource
    FastFileStorageClient fastFileStorageClient;

    @Value("${site.url}")
    private String fileUrl;


   /* @Override
    public BaseFile upload(BaseFileDTO dto) {
        BaseFile baseFile = new BaseFile();
        SmartBeanUtil.copyProperties(dto, baseFile);
        baseFile.setOperUser(SmartRequestTokenUtil.getRequestUserId());

        COSCredentials cred = new AnonymousCOSCredentials();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region(dto.getRegion()));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(cred, clientConfig);
        baseFile.setFileUrl(cosClient.getObjectUrl(dto.getBucket(), dto.getFileKey()).toString());
        save(baseFile);
        return baseFile;
    }*/

    @Override
    public List<String> fileListForUrl(String ids) {
        List<String> urlList = new ArrayList<>();
        List<BaseFileEntity> result = new ArrayList<>();
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtils.isNotEmpty(idList)) {
            Set<Long> idSet = idList.stream().map(item -> Long.valueOf(item)).collect(Collectors.toSet());
            LambdaQueryWrapper<BaseFileEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(BaseFileEntity::getId, idSet);
            wrapper.orderByDesc(BaseFileEntity::getUploadDate);
            result = this.list(wrapper);
            if (CollectionUtils.isNotEmpty(result)) {
                urlList = result.stream().map(BaseFileEntity::getFileUrl).collect(Collectors.toList());
            }
        }
        return urlList;
    }

    @Override
    public List<String> fileListForUrlByPrefix(String ids,String prefix) {
        List<String> urlList = new ArrayList<>();
        List<BaseFileEntity> result = new ArrayList<>();
        if(StringUtils.isBlank(ids) || StringUtils.isBlank(prefix)) return urlList;

        List<String> idList = Arrays.asList(ids.split(prefix));
        if (CollectionUtils.isNotEmpty(idList)) {
            Set<Long> idSet = idList.stream().map(item -> Long.valueOf(item)).collect(Collectors.toSet());
            LambdaQueryWrapper<BaseFileEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(BaseFileEntity::getId, idSet);
            wrapper.orderByDesc(BaseFileEntity::getUploadDate);
            result = this.list(wrapper);
            if (CollectionUtils.isNotEmpty(result)) {
                urlList = result.stream().map(BaseFileEntity::getFileUrl).collect(Collectors.toList());
            }
        }
        return urlList;
    }

    @Override
    public Map<Long, String> getFileUrlById(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyMap();
        List<BaseFileEntity> entities = this.list(Wrappers.lambdaQuery(BaseFileEntity.class).select(BaseFileEntity::getId, BaseFileEntity::getFileUrl).in(BaseFileEntity::getId, ids));
        if (CollectionUtils.isEmpty(entities)) return Collections.emptyMap();
        return entities.stream().collect(Collectors.toMap(BaseFileEntity::getId, BaseFileEntity::getFileUrl));
    }

    @Override
    public Map<Long, FileVO> getFileById(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) return Collections.emptyMap();
        List<BaseFileEntity> entities = this.list(Wrappers.lambdaQuery(BaseFileEntity.class).select(BaseFileEntity::getId, BaseFileEntity::getFileUrl, BaseFileEntity::getFileName)
                .in(BaseFileEntity::getId, ids).eq(BaseFileEntity::getIsValid,1));
        if (CollectionUtils.isEmpty(entities)) return Collections.emptyMap();
        return entities.stream().collect(Collectors.toMap(BaseFileEntity::getId, x -> new FileVO(x.getId(), x.getFileName(), x.getFileUrl())));
    }

    @Override
    public String getFileUrlById(Long id) {
        if(!CheckParamUtil.checkLongIdParam(id)){
            return null;
        }
        BaseFileEntity entity = this.getOne(Wrappers.lambdaQuery(BaseFileEntity.class).select(BaseFileEntity::getId, BaseFileEntity::getFileUrl).eq(BaseFileEntity::getId, id));
        return entity == null ? null : entity.getFileUrl();
    }

    @Override
    public FileVO getFileById(Long id) {
        BaseFileEntity entity = this.getOne(Wrappers.lambdaQuery(BaseFileEntity.class).select(BaseFileEntity::getId, BaseFileEntity::getFileUrl, BaseFileEntity::getFileName).eq(BaseFileEntity::getId, id));
        return entity == null ? null : new FileVO(entity.getId(), entity.getFileName(), entity.getFileUrl());
    }

    @Override
    public BaseFileEntity saveFile(String fileName, String fileUrl, String menuType, FileTypeEnum fileTypeEnum,Integer sensitive) {
        BaseFileEntity baseFile = new BaseFileEntity();
        baseFile.setId(UUIDUtils.randomNumber12());
        baseFile.setFileName(fileName);
        baseFile.setFileType(fileTypeEnum.getValue());
        baseFile.setFileUrl(fileUrl);
        baseFile.setMenuType(menuType);
        baseFile.setRequestId(sensitive.toString());
        baseFile.setOperUser(SmartRequestTokenUtil.getRequestUserId() == null ? 1 : SmartRequestTokenUtil.getRequestUserId());
        //baseFile.setFileSource("FastDfs");
        save(baseFile);
        return baseFile;
    }

    @Override
    public JsonResult<BaseFileEntity> saveVideoFileBySubject(MultipartFile file, String subjectCode) {
        JsonResult<BaseFileEntity> jsonResult = JsonResult.buildError(ResultCode.PARA_ERROR.code(), "基础管理-文件上传操作异常,请联系管理员");
        BaseFileEntity baseFile = new BaseFileEntity();
        try {
            String originalFilename = file.getOriginalFilename();
            String fileType = AssembleUtil.substrFilesPrefix(originalFilename);
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileSuffix, null);
            String fullPath = storePath.getFullPath();

            List<String> videlTypeList = CoreConstant.UPLOAD_VIDEO_TYPES;
            if (!videlTypeList.contains(fileType)) {
                jsonResult = JsonResult.buildError(ResultCode.PARA_ERROR.code(), "视频上传目前只支持MP4/FLV格式,请联系管理员");
                return jsonResult;
            }
            FileTypeEnum fileTypeEnum = FileTypeEnum.SUBJECT_VIDEO_TYPE;
            baseFile.setId(UUIDUtils.randomNumber12());
            baseFile.setFileName(originalFilename);
            //baseFile.setFileCode(fileTypeEnum.getCode());
            baseFile.setFileType(fileTypeEnum.getValue());
            baseFile.setFileUrl(fileUrl + fullPath);
            // 课程代码保存
            baseFile.setMenuType(subjectCode);
            baseFile.setOperUser(SmartRequestTokenUtil.getRequestUserId());
            baseFile.setUploadDate(new Date());
            baseMapper.insert(baseFile);
            jsonResult = JsonResult.buildSuccess(baseFile);
            log.info("call saveVideoFileBySubject in success,subjectCode:{},", subjectCode);
        } catch (Exception e) {
            log.error("call saveVideoFileBySubject exec fail ");
            return jsonResult;
        }

        return jsonResult;
    }

    @Override
    public BaseFileEntity uploadFile(InputStream inputStream, String fileName, Long fileSize, FileTypeEnum fileTypeEnum, String menuType) {
        String originalFilename = fileName;
        // 小程序文件名过长截取
        if (StringUtils.isNotEmpty(originalFilename) && originalFilename.length() > 60) {
            originalFilename = originalFilename.substring(originalFilename.length() - 60);
        }
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, fileSize, fileSuffix, null);
        String fullPath = storePath.getFullPath();
        return this.saveFile(originalFilename, fileUrl + fullPath, menuType, fileTypeEnum, 0);
    }

    @Override
    public void updateFile(Long id, String fileUrl) {
        BaseFileEntity baseFile = new BaseFileEntity();
        baseFile.setId(id);
        baseFile.setFileUrl(fileUrl);
        updateById(baseFile);
    }

    @Override
    public BaseFileEntity queryBaseFileByUrl(String url) {
        BaseFileEntity baseFile = null;
        List<BaseFileEntity> fileList = new ArrayList<>();
        QueryWrapper<BaseFileEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(url), "file_url", url);
        fileList = this.list(wrapper);
        if (CollectionUtil.isNotEmpty(fileList)) baseFile = fileList.get(0);
        return baseFile;
    }

    @Override
    public PageVO<BaseFilePageVo> queryPage(BaseFilePageAo pageAo) {
        Page<BaseFilePageVo> page = new Page<>(pageAo.getPageIndex(), pageAo.getPageSize());
        this.baseMapper.queryPage(page, pageAo);
        return new PageVO<>(page.getTotal(), page.getPages(), page.getRecords());
    }

    @Override
    public JsonResult<String> inOrUpFileInfo(BaseFileUpdateAo ao, MultipartFile file) throws Exception {
        JsonResult<String> res = null;
        String fileUrlData = "";
        if (ObjectUtil.isNotNull(file)) {
            String originalFilename = file.getOriginalFilename();
            // 小程序文件名过长截取
            if (StringUtils.isNotEmpty(originalFilename) && originalFilename.length() > 60) {
                originalFilename = originalFilename.substring(originalFilename.length() - 60, originalFilename.length());
            }
            String[] suffixs = {"pdf", "docx", "xlsx"};
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            double size = (double) file.getSize() / 1024;
            if (!ArrayUtil.contains(suffixs, fileSuffix) || size > 500)
                return JsonResult.buildError("仅支持上传pdf，docx， xlsx文件,且不超过500K!!!!");
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileSuffix, null);
            String fullPath = storePath.getFullPath();
            fileUrlData = fileUrl + fullPath;
        } else {
            if (StringUtils.isNotBlank(ao.getFileUrl())) {
                fileUrlData = ao.getFileUrl();
            }
        }
        if (ObjectUtil.isNotNull(ao.getId())) {  //更新
            BaseFileEntity baseFile = this.getById(ao.getId());
            BeanUtils.copyProperties(ao, baseFile);
            baseFile.setUploadDate(new Date());
            baseFile.setFileUrl(fileUrlData);
            baseFile.setOperUser(SmartRequestTokenUtil.getRequestUserId());
            baseFile.setUpdateTime(new Date());
            baseFile.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            this.baseMapper.updateById(baseFile);
        } else { //新增
            BaseFileEntity origin = new BaseFileEntity();
            BeanUtils.copyProperties(ao, origin);
            origin.setId(UUIDUtils.randomNumber12());
            origin.setUploadDate(new Date());
            origin.setFileUrl(fileUrlData);
            origin.setOperUser(SmartRequestTokenUtil.getRequestUserId());
            origin.setUpdateTime(new Date());
            origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            baseMapper.insert(origin);
        }
        return JsonResult.buildSuccess("文件管理 新增/编辑成功!");
    }

    @Override
    public JsonResult<BaseFileCosQueryVo> inOrUpFileInfoForCos(BaseFileUpdateCosAo ao) {
        BaseFileCosQueryVo cosQueryVo = new BaseFileCosQueryVo();
        BaseFileEntity origin = null;
        if (ObjectUtil.isNotNull(ao.getId())) {  //更新
            origin = this.getById(ao.getId());
            BeanUtils.copyProperties(ao, origin);
            origin.setUploadDate(new Date());
            origin.setOperUser(SmartRequestTokenUtil.getRequestUserId());
            origin.setUpdateTime(new Date());
            origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            Boolean uptRow = this.updateById(origin);
            log.info("call updateById in success, id:{},uptRow:{}", origin.getId(), uptRow);
        } else { //新增
            origin = new BaseFileEntity();
            BeanUtils.copyProperties(ao, origin);
            origin.setId(UUIDUtils.randomNumber12());
            origin.setUploadDate(new Date());
            origin.setOperUser(SmartRequestTokenUtil.getRequestUserId());
            origin.setUpdateTime(new Date());
            origin.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
            Boolean uptRow = this.save(origin);
            log.info("call save BaeFile in success, id:{},uptRow:{}", origin.getId(), uptRow);
        }
        SmartBeanUtil.copyProperties(origin, cosQueryVo);
        return JsonResult.buildSuccess(cosQueryVo);
    }

    @Override
    public JsonResult<String> deleteFile(Long id) {
        JsonResult<String> res = JsonResult.buildSuccess("删除成功!!!!");
        BaseFileEntity baseFile = this.getById(id);
        if (null == baseFile) return JsonResult.buildError("暂无数据!");
        baseFile.setIsValid(0);
        baseFile.setUpdateTime(new Date());
        baseFile.setUpdateUser(SmartRequestTokenUtil.getRequestUserId());
        this.baseMapper.updateById(baseFile);
        return res;
    }

    @Override
    public List<BaseFileExportVo> exportFileInfo(List<Long> fileIds) {
        List<BaseFileExportVo> exportVoList = new ArrayList<>();

        LambdaQueryWrapper<BaseFileEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseFileEntity::getIsValid, 1)
                .in(BaseFileEntity::getId, fileIds)
                .orderByDesc(BaseFileEntity::getUploadDate);
        List<BaseFileEntity> recordLst = baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(recordLst)) {
            Integer sum = 1;
            for (BaseFileEntity baseFile : recordLst) {
                BaseFileExportVo exportVo = new BaseFileExportVo();
                BeanUtil.copyProperties(baseFile, exportVo);
                exportVo.setSequence(sum.toString());
                exportVoList.add(exportVo);
                sum++;
            }
        }
        return exportVoList;
    }

    @Override
    public Map<Integer, Long> readExcelAndUploadPicture(MultipartFile file) {
        Map<Integer, Long> rowNumFileIdMap = new HashMap<>();
        try (InputStream fileInputStream = file.getInputStream()) {
            Map<Integer, XSSFPictureData> pictureDataMap = ExcelUtils.getPictures(fileInputStream);
            if (MapUtils.isNotEmpty(pictureDataMap)) {
                for (Map.Entry<Integer, XSSFPictureData> entry : pictureDataMap.entrySet()) {
                    byte[] data = entry.getValue().getData();
                    try (InputStream inputStream = new ByteArrayInputStream(data)) {
                        BaseFileEntity baseFileEntity = this.uploadFile(inputStream, ExcelUtils.getPictureFileName(entry.getValue()), (long) data.length, FileTypeEnum.PICTURE_TYPE, "IMAGE");
                        rowNumFileIdMap.put(entry.getKey(), baseFileEntity.getId());
                    }
                }
            }
        } catch (IOException e) {
            log.error("Fail to read excel file : ", e);
        }
        return rowNumFileIdMap;
    }

    @Override
    public JsonResult<BaseFileUploadVo> uploadFileByType(MultipartFile file, Integer fileType,String fileSuffix,Integer isSensitive) {
        String originalFilename = file.getOriginalFilename();
        // 小程序文件名过长截取
        if (StringUtils.isNotEmpty(originalFilename) && originalFilename.length() > 60) {
            originalFilename = originalFilename.substring(originalFilename.length() - 60, originalFilename.length());
        }
        String fileSuffixType = "";
        if(StringUtils.isNotBlank(fileSuffix)){
            fileSuffixType = fileSuffix;
        }else{
            fileSuffixType = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileSuffix, null);
            String fullPath = storePath.getFullPath();
            String menuType = fileType.equals(0) ? "IMAGE" : fileType.equals(1) ? "VIDEO" : "DOCUMENT";
            FileTypeEnum fileTypeEnum =fileType.equals(0) ? FileTypeEnum.PICTURE_TYPE : fileType.equals(1) ?
                    FileTypeEnum.VIDEO_TYPE : FileTypeEnum.DOCUMENT_TYPE;

            BaseFileEntity baseFile = saveFile(originalFilename, fileUrl + fullPath, menuType, fileTypeEnum,isSensitive);
            if(null != baseFile){
                BaseFileUploadVo uploadVo = new BaseFileUploadVo();
                SmartBeanUtil.copyProperties(baseFile,uploadVo);
                return JsonResult.buildDataSuccess(uploadVo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JsonResult.buildError("文件上传失败");
        }
        return JsonResult.buildError("文件上传失败");
    }

    /**
     * 文件下载
     * @param path
     * @return
     */
    public byte[] downloadFile(String path){
        String usePath = path.replace(fileUrl, "");
        int index = usePath.indexOf("/");
        String groupName = usePath.substring(0, index);
        String realPath = usePath.substring(index + 1);
        return fastFileStorageClient.downloadFile(groupName, realPath, new DownloadByteArray());
    }
}
