package com.ruoyi.web.controller.system;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class MapDataExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(MapDataExcelUtil.class);

    public static AjaxResult exportExcel(List<Map<String, Object>> dataList, String[] headers, String[] fieldKeys, String sheetName) {
        Workbook wb = null;
        FileOutputStream fos = null;
        try {
            wb = new HSSFWorkbook();
            Sheet sheet = wb.createSheet(sheetName);
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            CellStyle headerStyle = wb.createCellStyle();
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (Map<String, Object> row : dataList) {
                Row dataRow = sheet.createRow(rowNum++);
                for (int i = 0; i < fieldKeys.length; i++) {
                    Object val = row.get(fieldKeys[i]);
                    if (val != null) {
                        if (val instanceof Number) {
                            dataRow.createCell(i).setCellValue(((Number) val).doubleValue());
                        } else {
                            dataRow.createCell(i).setCellValue(String.valueOf(val));
                        }
                    } else {
                        dataRow.createCell(i).setCellValue("");
                    }
                }
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i, true);
            }

            String fileName = sheetName + "_" + System.currentTimeMillis() + ".xls";
            String downloadPath = RuoYiConfig.getDownloadPath();
            File desc = new File(downloadPath + fileName);
            if (!desc.getParentFile().exists()) {
                desc.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(desc);
            wb.write(fos);
            return AjaxResult.success(fileName);
        } catch (Exception e) {
            log.error("导出Excel异常{}", e.getMessage());
            return AjaxResult.error("导出Excel失败，请联系网站管理员！");
        } finally {
            if (fos != null) {
                try { fos.close(); } catch (Exception ignored) {}
            }
            if (wb != null) {
                try { wb.close(); } catch (Exception ignored) {}
            }
        }
    }
}