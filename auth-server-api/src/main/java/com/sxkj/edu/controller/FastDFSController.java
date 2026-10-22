package com.sxkj.edu.controller;


import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.sxkj.edu.common.anno.AccessLimit;
import com.sxkj.edu.common.anno.NoNeedLogin;
import com.sxkj.edu.common.anno.OperateLog;
import com.sxkj.edu.common.base.constants.AmqpConstant;
import com.sxkj.edu.common.base.constants.ResultCode;
import com.sxkj.edu.common.base.constants.SwaggerTagConst;
import com.sxkj.edu.common.base.enums.FileTypeEnum;
import com.sxkj.edu.common.core.JsonResult;
import com.sxkj.edu.domain.ao.file.BaseFileUploadAo;
import com.sxkj.edu.domain.sys.entity.BaseFileEntity;
import com.sxkj.edu.domain.vo.BaseFileUploadVo;
import com.sxkj.edu.service.BaseFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @author zhengqn
 * @since 2022-12-21
 */
@RestController
@RequestMapping("/api/file")
@PropertySource({"classpath:base.properties"})
@Api(tags ={SwaggerTagConst.Admin.FILE_UPLOAD})
@OperateLog
@Slf4j
public class FastDFSController {

    @Value("${site.url}")
    private String fileUrl;

    @Resource
    FastFileStorageClient fastFileStorageClient;

    @Resource
    BaseFileService baseFileService;

    @PostMapping("/upload/data")
    @ApiOperation("文件上传")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件上传" ,businessType = "文件上传")
    @NoNeedLogin
    @AccessLimit(seconds = 15, maxCount = 7, desc = "您的频率过快，请稍微在试")
    public JsonResult<BaseFileEntity> uploadFastdfs(@NotBlank(message = "类型不能为空") @ApiParam(value = "0:图片，1：视频，2-文档,3-其他", required = true) @RequestParam Integer type,
                                             @RequestParam("file") MultipartFile file) throws Exception {
        BaseFileEntity baseFile = null;
        try {
            String originalFilename = file.getOriginalFilename();
            // 小程序文件名过长截取
            if (StringUtils.isNotEmpty(originalFilename) && originalFilename.length() > 60) {
                originalFilename = originalFilename.substring(originalFilename.length() - 60, originalFilename.length());
            }
            String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileSuffix, null);
            String fullPath = storePath.getFullPath();
            String menuType = type.equals(0) ? "IMAGE" : type.equals(1) ? "VIDEO" : "DOCUMENT";
            FileTypeEnum fileTypeEnum = type.equals(0) ? FileTypeEnum.PICTURE_TYPE : type.equals(1) ? FileTypeEnum.VIDEO_TYPE : FileTypeEnum.DOCUMENT_TYPE;

            baseFile = baseFileService.saveFile(originalFilename, fileUrl + fullPath, menuType, fileTypeEnum,0);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件上传失败");
        }
        return JsonResult.buildSuccess(baseFile);
    }

    @PostMapping("/uploadMain/data/fileType")
    @ApiOperation("文件管理-上传")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件管理-上传" ,businessType = "文件上传")
    public JsonResult<BaseFileUploadVo> uploadFastdfsByType(@ApiParam(value = "0:图片，1：视频，2-文档,3-其他", required = true) @RequestParam Integer fileType,
                                                            @ApiParam(value = "文件后缀名", required = false) @RequestParam String fileSuffix,
                                                            @ApiParam(value = "是否敏感文件,1-是，0-否", required = false) @RequestParam Integer isSensitive,
                                                            @RequestParam("file") MultipartFile file){
        return baseFileService.uploadFileByType(file, fileType,fileSuffix,isSensitive);
    }


    @GetMapping("/download")
    @ApiOperation("下载文件")
    @OperateLog(type = AmqpConstant.OPERATION, value = "文件下载" ,businessType = "文件下载")
    public JsonResult download(@NotBlank(message = "path不能为空") @ApiParam(value = "文件上传后返回的路径", required = true) @RequestParam String path, HttpServletRequest request, HttpServletResponse response) {
        try {
            String usePath = path.replace(fileUrl, "");
            int index = usePath.indexOf("/");
            String groupName = usePath.substring(0, index);
            String realPath = usePath.substring(index + 1);
            String fileName = usePath.substring(usePath.lastIndexOf("/") + 1);
            byte[] bytes = fastFileStorageClient.downloadFile(groupName, realPath, new DownloadByteArray());
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.write(bytes, outputStream);
        } catch (Exception e) {
            return JsonResult.buildError(ResultCode.ERROR_PARAM.code(), "文件错误！");
        }
        return null;
    }

}

