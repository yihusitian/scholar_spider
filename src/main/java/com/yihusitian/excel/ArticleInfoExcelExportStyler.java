package com.yihusitian.excel;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

import java.util.Objects;

/**
 * @Description
 * @Author leeho
 * @Date 2022/7/9 下午1:57
 */
public class ArticleInfoExcelExportStyler extends AbstractExcelExportStyler implements IExcelExportStyler {

    public ArticleInfoExcelExportStyler(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getHeaderStyle(short i) {
      return null;
    }

    @Override
    public CellStyle getTitleStyle(short i) {
        CellStyle cellStyle = workbook.createCellStyle();
        //获取单元格内容对象
        Font font = workbook.createFont();
        //设置单元格背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置单元格字体颜色
        font.setColor(IndexedColors.PINK.getIndex());
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        return cellStyle;
    }

    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        CellStyle cellStyle = workbook.createCellStyle();
        //获取单元格内容对象
        Font font = workbook.createFont();
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        font.setFontHeightInPoints((short) 12);
        if (Objects.nonNull(entity)) {
            if (entity.getOrderNum() < 2 || entity.getOrderNum() > 4) {
                font.setBold(true);
                font.setFontHeightInPoints((short) 14);
            }

            if (entity.getOrderNum() == 4) {
                font.setColor(IndexedColors.BLUE.getIndex());
                font.setUnderline((byte) 1);
            }
        }
        cellStyle.setFont(font);
        return cellStyle;
    }


}
