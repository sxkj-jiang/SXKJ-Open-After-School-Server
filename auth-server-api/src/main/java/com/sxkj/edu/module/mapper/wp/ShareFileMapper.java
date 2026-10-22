package com.sxkj.edu.module.mapper.wp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxkj.edu.domain.ao.wp.SaveWpShareFileAo;
import com.sxkj.edu.domain.vo.wp.ShareFileListVO;
import com.sxkj.edu.module.business.entity.wp.WpShareFilePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ShareFileMapper extends BaseMapper<WpShareFilePo> {

    List<ShareFileListVO> selectShareFileList(@Param("shareBatchNum") String shareBatchNum, @Param("shareFilePath") String filePath);

    Integer countShareFileNum(@Param("query") SaveWpShareFileAo query);
}
