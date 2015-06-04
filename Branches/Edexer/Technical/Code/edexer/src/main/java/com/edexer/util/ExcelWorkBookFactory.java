package com.edexer.util;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWorkBookFactory {

	
	public static Workbook getInstance(FileInputStream fis, String fileExt) throws Exception{
		Workbook workbook;
		if(FileReader.EXLEXTENSION.equalsIgnoreCase(fileExt)){
			workbook = new HSSFWorkbook(fis);
		}else if(FileReader.EXLXEXTENSION.equalsIgnoreCase(fileExt)){
			workbook = new XSSFWorkbook(fis);
		}
		else{
			throw new Exception("Invalid file extension...");
		}
		return workbook;
	}
}
