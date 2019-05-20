package com.hyu.excel;

import javax.servlet.http.*;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.*;
import java.lang.reflect.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.slf4j.*;

public class ExportExcel
{
    private static final Logger logger;
    private static WritableFont NormalFont;
    private static WritableFont BoldFont;
    private static WritableCellFormat wcf_center;
    private static WritableCellFormat wcf_left;
    public static final String FILEDNAME_SERIALNUMBER = "serialNumber";
    public static final int DEFAULT_BEGIN_ROW = 0;
    public static final String DEFAULT_ERROR_MSG = "";
    
    public static final String exportExcel(final String fileName, final String[] Title, final List<?> listContent, final HttpServletResponse response, final int beginRows) {
        final String result = "\u7cfb\u7edf\u63d0\u793a\uff1aExcel\u6587\u4ef6\u5bfc\u51fa\u6210\u529f\uff01";
        try {
            final OutputStream os = response.getOutputStream();
            initResponse(response, fileName);
            final WritableWorkbook workbook = Workbook.createWorkbook(os);
            final WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            final SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);
            for (int i = 0; i < Title.length; ++i) {
                sheet.addCell(new Label(i, beginRows, Title[i], ExportExcel.wcf_center));
            }
            Field[] fields = null;
            int j = beginRows + 1;
            for (final Object obj : listContent) {
                fields = obj.getClass().getDeclaredFields();
                int k = 0;
                for (final Field v : fields) {
                    v.setAccessible(true);
                    final Object va = v.get(obj);
                    sheet.addCell(new Label(k, j, formatObjectValue(va), ExportExcel.wcf_left));
                    ++k;
                }
                ++j;
            }
            workbook.write();
            workbook.close();
        }
        catch (Exception e) {
            ExportExcel.logger.warn("\u7cfb\u7edf\u63d0\u793a\uff1aExcel\u6587\u4ef6\u5bfc\u51fa\u5931\u8d25\uff0c\u539f\u56e0\uff1a" + e.toString());
            e.printStackTrace();
        }
        return result;
    }
    
    public static final String exportExcel(final String fileName, final String[] Title, final String[] fieldNames, final List<?> listContent, final HttpServletResponse response, final int beginRows) {
        final String result = "\u7cfb\u7edf\u63d0\u793a\uff1aExcel\u6587\u4ef6\u5bfc\u51fa\u6210\u529f\uff01";
        try {
            final OutputStream os = response.getOutputStream();
            initResponse(response, fileName);
            final WritableWorkbook workbook = Workbook.createWorkbook(os);
            final WritableSheet sheet = workbook.createSheet("Sheet1", 0);
            final SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);
            for (int i = 0; i < Title.length; ++i) {
                sheet.addCell(new Label(i, beginRows, Title[i], ExportExcel.wcf_center));
            }
            int i = beginRows + 1;
            for (final Object obj : listContent) {
                int j = 0;
                for (final String fieldName : fieldNames) {
                    Field field = null;
                    if (fieldName.equals(FILEDNAME_SERIALNUMBER)) {
                        sheet.addCell(new Label(j, i, String.valueOf(i - beginRows), ExportExcel.wcf_left));
                        ++j;
                    }
                    else {
                        try {
                            field = obj.getClass().getDeclaredField(fieldName);
                        }
                        catch (NoSuchFieldException e2) {
                            try {
                                field = obj.getClass().getSuperclass().getDeclaredField(fieldName);
                            }
                            catch (Exception e3) {
                                sheet.addCell(new Label(j, i, "", ExportExcel.wcf_left));
                                ++j;
                            }
                        }
                        field.setAccessible(true);
                        final Object va = field.get(obj);
                        sheet.addCell(new Label(j, i, formatObjectValue(va), ExportExcel.wcf_left));
                        ++j;
                    }
                }
                ++i;
            }
            workbook.write();
            workbook.close();
        }
        catch (Exception e) {
            ExportExcel.logger.warn("\u7cfb\u7edf\u63d0\u793a\uff1aExcel\u6587\u4ef6\u5bfc\u51fa\u5931\u8d25\uff0c\u539f\u56e0\uff1a" + e.toString());
            e.printStackTrace();
        }
        return result;
    }
    
    static void initResponse(final HttpServletResponse response, final String fileName) throws UnsupportedEncodingException {
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
        response.setContentType("application/msexcel");
    }
    
    static String formatObjectValue(final Object o) {
        if (o == null) {
            return "";
        }
        if (o instanceof Date) {
            final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format((Date)o);
        }
        return o.toString();
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)ExportExcel.class);
        ExportExcel.NormalFont = new WritableFont(WritableFont.ARIAL, 10);
        ExportExcel.BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        try {
            (ExportExcel.wcf_center = new WritableCellFormat(ExportExcel.BoldFont)).setBorder(Border.ALL, BorderLineStyle.THIN);
            ExportExcel.wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE);
            ExportExcel.wcf_center.setAlignment(Alignment.CENTRE);
            ExportExcel.wcf_center.setWrap(false);
            (ExportExcel.wcf_left = new WritableCellFormat(ExportExcel.NormalFont)).setBorder(Border.NONE, BorderLineStyle.THIN);
            ExportExcel.wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE);
            ExportExcel.wcf_left.setAlignment(Alignment.LEFT);
            ExportExcel.wcf_left.setWrap(false);
        }
        catch (Exception ex) {}
    }
}