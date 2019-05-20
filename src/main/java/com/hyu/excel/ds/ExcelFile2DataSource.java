package com.hyu.excel.ds;

import com.hyu.excel.entity.FileModelColumn;
import com.hyu.excel.exception.EndOfFlowException;
import com.hyu.excel.exception.ParseFileException;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.web.multipart.*;
import java.text.*;
import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.*;
import org.apache.poi.openxml4j.opc.*;
import java.io.*;
import org.apache.poi.openxml4j.exceptions.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.*;

public class ExcelFile2DataSource<T> extends DataSource<T>
{
    private static Logger logger;
    private Workbook wb;
    private Sheet st;
    private HSSFSheet hst;
    private XSSFSheet xst;
    private HSSFWorkbook hwb;
    private XSSFWorkbook rwb;
    private MultipartFile mf;
    private String fn;
    DecimalFormat df;
    SimpleDateFormat sdf;
    DecimalFormat nf;
    
    public ExcelFile2DataSource(final Class<T> c, final MultipartFile mf) {
        super(c);
        this.wb = null;
        this.st = null;
        this.hst = null;
        this.xst = null;
        this.hwb = null;
        this.rwb = null;
        this.mf = null;
        this.fn = null;
        this.df = new DecimalFormat("0");
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.nf = new DecimalFormat("0.00");
        this.mf = mf;
    }
    
    public ExcelFile2DataSource(final Class<T> c, final String fn) {
        super(c);
        this.wb = null;
        this.st = null;
        this.hst = null;
        this.xst = null;
        this.hwb = null;
        this.rwb = null;
        this.mf = null;
        this.fn = null;
        this.df = new DecimalFormat("0");
        this.sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.nf = new DecimalFormat("0.00");
        this.fn = fn;
    }
    
    @Override
    public void onInit() {
        try {
            ExcelFile2DataSource.logger.debug("\u6570\u636e\u6e90\u521d\u59cb\u5316...");
            if (this.fn == null) {
                this.wb = this.createworkbook(this.mf.getInputStream());
                this.st = this.wb.getSheetAt(0);
            }
            else {
                this.wb = this.createworkbook(new FileInputStream(this.fn));
                this.st = this.wb.getSheetAt(0);
            }
        }
        catch (IllegalArgumentException e) {
            throw new ParseFileException("\u4f60\u7684excel\u7248\u672c\u76ee\u524dpoi\u89e3\u6790\u4e0d\u4e86");
        }
        catch (Exception e2) {
            throw new ParseFileException();
        }
    }
    
    public Workbook createworkbook(InputStream in) throws IOException, InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return (Workbook)new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return (Workbook)new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException("\u4f60\u7684excel\u7248\u672c\u76ee\u524dpoi\u89e3\u6790\u4e0d\u4e86");
    }
    
    @Override
    public Object getColumnValue(final int row, final FileModelColumn mc) {
        Row rowCell = null;
        Cell cell = null;
        try {
            rowCell = this.st.getRow(row - 1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new EndOfFlowException();
        }
        String columnValue = "";
        if (this.st.getLastRowNum() < row - 1 && rowCell == null) {
            throw new EndOfFlowException();
        }
        if (rowCell.getLastCellNum() > mc.getCol() - 1) {
            cell = rowCell.getCell(mc.getCol() - 1);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case 1: {
                        columnValue = cell.getStringCellValue();
                        break;
                    }
                    case 0: {
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            cell.setCellType(1);
                            columnValue = cell.getStringCellValue();
                            break;
                        }
                        if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            cell.setCellType(1);
                            columnValue = cell.getStringCellValue();
                            break;
                        }
                        cell.setCellType(1);
                        columnValue = cell.getStringCellValue();
                        break;
                    }
                    case 4: {
                        cell.setCellType(1);
                        columnValue = cell.getStringCellValue();
                        break;
                    }
                    case 3: {
                        columnValue = "";
                        break;
                    }
                    default: {
                        System.out.println(row + "\u884c" + mc.getCol() + " \u5217 is default type" + ":" + cell.getStringCellValue());
                        columnValue = cell.toString();
                        break;
                    }
                }
            }
        }
        return columnValue;
    }
    
    public String changeCellToString(final HSSFCell cell) {
        String returnValue = "";
        if (null != cell) {
            switch (cell.getCellType()) {
                case 0: {
                    final Double doubleValue = cell.getNumericCellValue();
                    String str = doubleValue.toString();
                    if (str.contains(".0")) {
                        str = str.replace(".0", "");
                    }
                    final Integer intValue = Integer.parseInt(str);
                    returnValue = intValue.toString();
                    break;
                }
                case 1: {
                    returnValue = cell.getStringCellValue();
                    break;
                }
                case 4: {
                    final Boolean booleanValue = cell.getBooleanCellValue();
                    returnValue = booleanValue.toString();
                    break;
                }
                case 3: {
                    returnValue = "";
                    break;
                }
                case 2: {
                    returnValue = cell.getCellFormula();
                    break;
                }
                case 5: {
                    returnValue = "";
                    break;
                }
                default: {
                    System.out.println("\u672a\u77e5\u7c7b\u578b");
                    break;
                }
            }
        }
        return returnValue;
    }
    
    @Override
    public void release() {
        this.mf = null;
        this.st = null;
    }
    
    static {
        ExcelFile2DataSource.logger = LoggerFactory.getLogger((Class)ExcelFile2DataSource.class);
    }
}