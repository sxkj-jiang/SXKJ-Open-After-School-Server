package com.sxkj.edu.module.mapper.wp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxkj.edu.domain.ao.wp.QueryShareFileAo;
import com.sxkj.edu.domain.vo.wp.QueryRecoveryPageVO;
import com.sxkj.edu.domain.vo.wp.ShareListVO;
import com.sxkj.edu.module.business.entity.wp.WpRecoveryFilePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface RecoveryFileMapper extends BaseMapper<WpRecoveryFilePo> {

//    List<RecoveryFileListVo> selectRecoveryFileList(@Param("userId") Long userId);

    Page<QueryRecoveryPageVO> selectFileRecoveryPage(Page<ShareListVO> page, @Param("query") QueryShareFileAo query);
}
