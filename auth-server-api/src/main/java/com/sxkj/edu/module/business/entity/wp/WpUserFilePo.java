package com.sxkj.edu.module.business.entity.wp;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sxkj.edu.common.base.domain.BaseAssignIdEntity;
import com.sxkj.edu.common.base.domain.BaseEntity;
import com.sxkj.edu.common.utils.UUIDUtils;
import com.sxkj.edu.config.SmartRequestTokenUtil;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@TableName("t_wp_user_file")
public class WpUserFilePo extends BaseAssignIdEntity {


    private Long userId;

    // 标签id
    private Long labelId;

    // 文件类型(1公开,2已分享)，管理员可以查询所有，只对个人访问时生效
    private Integer shareType;

    // 文件MD5
    private String fileMd5;

    private String fileName;

    private String filePath;

    // 本地服务器文件路径
    private String localPath;

    private String fileUrl;

    private String extendName;

    // 1-目录，0-文件
    private Integer isDir;

    private Long fileSize;

    // 文件存储类型，1-图片，2-文档，3-视频，4-音频，5-其他
    private Integer storageType;

    private Date deleteTime;

    private String deleteBatchNum;

    // 学校id
    private Long schoolId;

    // 文件状态：0-草稿,1-正常,2-回收站；3-自动清理；4-手动清理
    private Integer status;


    public WpUserFilePo() {};
    public WpUserFilePo(String filePath, String fileName, String extendName, Integer isDir, Long userId, String md5) {
        this.setId(UUIDUtils.randomNumber12());
        this.userId = userId;
        this.fileMd5 = md5;
        this.filePath = filePath;
        this.fileName = fileName;
        this.extendName = extendName;
        this.shareType = 1;
        this.isDir = isDir;
        this.status = 0;// 草稿
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        this.setCreateTime(new Date());
        this.setIsValid(1);
    }

    public WpUserFilePo(String filePath, String fileUrl, String fileName, String extendName, Integer isDir, Long userId, String md5) {
        this.userId = userId;
        this.fileUrl = fileUrl;
        this.fileMd5 = md5;
        this.filePath = filePath;
        this.fileName = fileName;
        this.extendName = extendName;
        this.shareType = 1;
        this.isDir = isDir;
        this.status = 0;
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        this.setCreateTime(new Date());
        this.setIsValid(1);
    }

    public WpUserFilePo(String localPath, String filePath, String fileUrl, String fileName, String extendName, Integer isDir, Long userId, String md5) {
        this.userId = userId;
        this.localPath = localPath;
        this.fileUrl = fileUrl;
        this.fileMd5 = md5;
        this.filePath = filePath;
        this.fileName = fileName;
        this.extendName = extendName;
        this.shareType = 1;
        this.isDir = isDir;
        this.status = 0;
        this.setCreateUser(SmartRequestTokenUtil.getRequestUserId());
        this.setCreateTime(new Date());
        this.setIsValid(1);
    }

    public boolean isDirectory() {
        return this.isDir == 1;
    }

    public boolean isFile() {
        return this.isDir == 0;
    }

}
