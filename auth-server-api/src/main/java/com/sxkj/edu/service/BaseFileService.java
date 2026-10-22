package com.sxkj.edu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxkj.edu.common.base.enums.FileTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.common.page.PageVO;
import com.sxkj.edu.domain.ao.file.BaseFileUploadAo;
import com.sxkj.edu.domain.base.*;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import com.sxkj.edu.domain.vo.BaseFileUploadVo;
import com.sxkj.edu.domain.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础-上传附件表 服务类
 * </p>
 *
 * @author zhengqn
 * @since 2022-12-30
 */
public interface BaseFileService extends IService<BaseFileEntity> {


    List<String> fileListForUrl(String ids);

    // 根据ID获取文件URL，分隔符自定义
    List<String> fileListForUrlByPrefix(String ids,String prefix);

    Map<Long, String> getFileUrlById(List<Long> ids);

    Map<Long, FileVO> getFileById(List<Long> ids);

    // 根据ID获取文件URL
    String getFileUrlById(Long id);

    FileVO getFileById(Long id);

    BaseFileEntity saveFile(String fileName, String fileUrl, String menuType, FileTypeEnum fileTypeEnum,Integer sendType);

    JsonResult<BaseFileEntity> saveVideoFileBySubject(MultipartFile file, String subjectCode);

    BaseFileEntity uploadFile(InputStream inputStream, String fileName, Long fileSize, FileTypeEnum fileTypeEnum, String menuType);

    void updateFile(Long id, String fileUrl);

    BaseFileEntity queryBaseFileByUrl(String url);

    /**
     * 文件管理-分页查询
     *
     * @param pageAo
     * @return
     */
    PageVO<BaseFilePageVo> queryPage(BaseFilePageAo pageAo);

    /**
     * 文件管理-新增/编辑
     *
     * @param ao
     * @return
     */
    JsonResult<String> inOrUpFileInfo(BaseFileUpdateAo ao, MultipartFile file) throws Exception;

    JsonResult<BaseFileCosQueryVo> inOrUpFileInfoForCos(BaseFileUpdateCosAo ao);

    /**
     * 文件管理-删除
     *
     * @param id
     * @return
     */
    JsonResult<String> deleteFile(Long id);

    /**
     * 文件管理-批量导出
     *
     * @param fileIds
     * @return
     */
    List<BaseFileExportVo> exportFileInfo(List<Long> fileIds);

    /**
     * 读取excel中的图片并上传
     *
     * @param file 文件
     * @return key-行数 value-文件ID
     */
    Map<Integer, Long> readExcelAndUploadPicture(MultipartFile file);

    JsonResult<BaseFileUploadVo> uploadFileByType(MultipartFile file, Integer fileType,String fileSuffix,Integer isSensitive);

    /**
     * 文件下载
     * @param path
     * @return
     */
    byte[] downloadFile(String path);
}
