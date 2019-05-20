package com.hyu.excel;

import com.hyu.excel.ds.DataSource;
import com.hyu.excel.entity.FileModel;
import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.entity.ImportResult;
import com.hyu.excel.exception.DataException;
import com.hyu.excel.exception.EndOfFlowException;
import jxl.write.*;
import jxl.*;

import java.io.OutputStream;
import java.util.*;
import org.slf4j.*;

public class ImportExportManager
{
    private static Logger logger;
    private FileModel model;

    private ImportExportManager(final FileModel model) {
        this.model = model;
    }

    public FileModel getModel() {
        return this.model;
    }

    public void setModel(final FileModel model) {
        this.model = model;
    }

    public static ImportExportManager getInstance(final FileModel model) {
        ImportExportManager.logger.debug("\u5b9e\u4f8b\u5316\u5bfc\u5165\u5bfc\u51fa\u7ba1\u7406\u5de5\u5177");
        final ImportExportManager ie = new ImportExportManager(model);
        return ie;
    }

    public <T> ImportResult<T> importFromModel(final DataSource<T> ds) {
        ds.init();
        final ImportResult<T> result = new ImportResult<T>();
        try {
            int i = 1;
            while (true) {
                if (i >= this.model.getStartRow()) {
                    try {
                        result.addSuccessResult(ds.getRow(this.model, i));
                    }
                    catch (DataException e) {
                        result.addErrorResult(e.getSource());
                        ImportExportManager.logger.error(e.getMessage());
                    }
                }
                else {
                    ds.skip();
                }
                ++i;
            }
        }
        catch (EndOfFlowException ee) {
            ImportExportManager.logger.debug(ee.getMessage());
        }
        finally {
            ds.release();
        }
        return result;
    }

    public <T> WritableWorkbook exportAsExcel(final OutputStream out, final DataSource<T> ds) throws Exception {
        final WritableWorkbook workbook = Workbook.createWorkbook(out);
        return this.exportAsExcel(workbook, ds);
    }

    public <T> WritableWorkbook exportAsExcel(final WritableWorkbook workbook, final DataSource<T> ds) throws Exception {
        final WritableSheet sheet = (workbook.getNumberOfSheets() > 0) ? workbook.getSheet(0) : workbook.createSheet("Sheet1", 0);
        final SheetSettings sheetset = sheet.getSettings();
        sheetset.setProtected(false);
        final List<FileModelColumn> fmcl = this.model.getFileModelColumns();
        for (final FileModelColumn fileModelColumn : fmcl) {
            sheet.addCell((WritableCell)new Label(fileModelColumn.getCol() - 1, this.model.getStartRow() - 2, fileModelColumn.getDescription()));
        }
        int i = this.model.getStartRow() - 1;
        int r = 0;
        try {
            while (ds.getRow(null, r) != null) {
                for (final FileModelColumn fileModelColumn2 : fmcl) {
                    sheet.addCell((WritableCell)new Label(fileModelColumn2.getCol() - 1, i, fileModelColumn2.getRenderer().render(ds.getConverterFactory().getConverter(fileModelColumn2.getDataType()).asString(ds.getColumnValue(r, fileModelColumn2)))));
                }
                ++i;
                ++r;
            }
        }
        catch (EndOfFlowException ex) {}
        catch (Exception e) {
            ImportExportManager.logger.error(e.getMessage(), (Throwable)e);
        }
        return workbook;
    }

    public <T> void exportExcelToModel(final OutputStream out, final DataSource<T> ds) {
        try {
            this.exportExcelToModel(this.exportAsExcel(out, ds), ds);
        }
        catch (Exception e) {
            ImportExportManager.logger.warn("\u7cfb\u7edf\u63d0\u793a\uff1aExcel\u6587\u4ef6\u5bfc\u51fa\u5931\u8d25\uff0c\u539f\u56e0\uff1a" + e.getMessage(), (Throwable)e);
        }
    }

    public <T> void exportExcelToModel(final WritableWorkbook workbook, final DataSource<T> ds) {
        try {
            workbook.write();
            workbook.close();
        }
        catch (Exception e) {
            ImportExportManager.logger.warn("\u7cfb\u7edf\u63d0\u793a\uff1aExcel\u6587\u4ef6\u5bfc\u51fa\u5931\u8d25\uff0c\u539f\u56e0\uff1a" + e.getMessage(), (Throwable)e);
        }
    }

    public static void main(final String[] args) {
    }

    static {
        ImportExportManager.logger = LoggerFactory.getLogger((Class)ImportExportManager.class);
    }
}