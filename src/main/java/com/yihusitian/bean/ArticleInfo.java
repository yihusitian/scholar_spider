package com.yihusitian.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description
 * @Author LeeHo
 * @Date 2022/7/8 11:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ArticleInfo {
    //id
    private String cid;

    private int pageNo;

    //标题
    @Excel(name = "标题", orderNum = "0", width = 120)
    private String title;

    //标题
    @Excel(name = "标题翻译", orderNum = "1", width = 120)
    private String titleTrans;

    //摘要
    @Excel(name = "摘要", orderNum = "2", width = 180)
    private String abstractInfo;

    //摘要
    @Excel(name = "摘要翻译", orderNum = "3", width = 180)
    private String abstractInfoTrans;

    //文献链接
    @Excel(name = "文献链接", orderNum = "4", width = 150, isHyperlink = true)
    private String linkUrl;

    //作者
    @Excel(name = "作者", orderNum = "5", width = 100)
    private String author;

    //年份
    @Excel(name = "年份", orderNum = "6", width = 20)
    private String year;

}