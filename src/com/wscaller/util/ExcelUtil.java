package com.wscaller.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by dqwork on 2016/11/24.
 */
public class ExcelUtil {

    public void readExcel(File file) throws IOException {
        HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
        HSSFSheet sheet = hwb.getSheetAt(0);
        HSSFRow row = null;
        int num=0;
        for (int i = 2; i < sheet.getPhysicalNumberOfRows(); i++) {
            row = sheet.getRow(i);
            if (row == null ) {
                break;
            }
            //是否是空行
            boolean flag = true;
            for(int w=0;w<14;w++){
                HSSFCell cell1=row.getCell(w);
                String value1 = "";
                if(cell1 != null){
                    switch (cell1.getCellType()) {
                        case Cell.CELL_TYPE_STRING:
                            value1 = cell1.getStringCellValue();
                            break;
                        case Cell.CELL_TYPE_NUMERIC:
                            value1 = String.valueOf((int) cell1.getNumericCellValue());
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:
                            value1 = String.valueOf(cell1.getBooleanCellValue());
                            break;
                        case Cell.CELL_TYPE_FORMULA:
                            value1 = String.valueOf(cell1.getCellFormula());
                            break;
                        //case Cell.CELL_TYPE_BLANK:
                        //    break;
                        default:
                            break;
                    }

                    if(!value1.trim().equals("")){
                        flag = false;
                        break;
                    }
                }
            }

            if (flag) {
                break;
            }
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j <=27; j++) {
                HSSFCell cell = row.getCell(j);
                String value;
                if(cell==null){
                    value="null";
                }else{
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    value = "'"+cell.getStringCellValue().trim()+"'";
//                    value = "imp_"+cell.getStringCellValue().trim()+" NVARCHAR2(100),\n";
                }
                if(j>0)buffer.append(",");
                buffer.append(value);
            }
            num++;
            String sql = "insert into EME_WEBCAM2 (webcam_id, imp_value, imp_type, imp_chanid, webcam_name, imp_nodeno, imp_chanindex, imp_isptz, imp_codetype, imp_dvrid, imp_dvrtyp, imp_dvrip, imp_dvrport, imp_dvrname, imp_usetype, imp_externalip, imp_externalport, imp_orgid, imp_gjz, imp_index, imp_id, imp_vischanid, imp_vischanno, imp__nchanid_physical, imp_dvruser, imp_dvrpasswd, location, area, corp_name, longitude, latitude, sync_operate, sync_last_time)";
            sql+=" values(eme_webcam_seq.nextval,"+buffer+",null,null,1,sysdate);";
            System.out.println(sql);
        }
        System.out.print("已读"+num+"条");

    }
    public static void main(String[] args) throws IOException {
        ExcelUtil util = new ExcelUtil();
        File file = new File("F:\\1111111111\\webcam.xls");
        util.readExcel(file);
    }
}
