package com.yihusitian.excel;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import com.yihusitian.bean.ArticleInfo;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/9 下午1:48
 */
public class ArticleInfoExcelDataHandler extends ExcelDataHandlerDefaultImpl<ArticleInfo> {

    @Override
    public Hyperlink getHyperlink(CreationHelper creationHelper, ArticleInfo obj, String name, Object value) {
        Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
        hyperlink.setLabel(name);
        hyperlink.setAddress((String) value);
        return hyperlink;
    }
}
