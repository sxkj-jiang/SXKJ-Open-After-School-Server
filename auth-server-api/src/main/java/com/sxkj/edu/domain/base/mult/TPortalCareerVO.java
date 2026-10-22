package com.sxkj.edu.domain.base.mult;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: TPortalCareerVO
 * @Author YZY
 * @Date 2024年07月09日
 */
@Data
public class TPortalCareerVO implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("职业名称")
    private String name;

    @ApiModelProperty("职业代码")
    private String code;

    @ApiModelProperty("行业id")
    private Long industryId;

    @ApiModelProperty("所属行业： 1. 农、林、牧、渔业；2. 采矿业；3. 制造业；4. 电力、热力、燃气及水生产和供应业；5. 建筑业；6. 批发和零售业；7. 交通运输、仓储和邮政业；8. 住宿和餐饮业；9. 信息传输、软件和信息技术服务业；10. 金融业；11. 房地产业；12. 租赁和商务服务业；13. 科学研究和技术服务业；14. 水利、环境和公共设施管理业；15. 居民服务、修理和其他服务业；16. 教育；17. 卫生和社会工作；18. 文化、体育和娱乐业；19. 公共管理、社会保障和社会组织；20. 国际组织；21. 其他")
    private String industryName;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "排序")
    private Integer sort;

}
