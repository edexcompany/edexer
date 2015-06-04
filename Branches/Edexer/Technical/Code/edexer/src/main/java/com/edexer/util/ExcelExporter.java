package com.edexer.util;

import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;

import com.edexer.model.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

public class ExcelExporter {

	private static final String[] bcExportTitles = { "First Name", "Last Name",
			"Mobile 1", "Mobile 2", "Phone 1", "Phone 2", "Fax 1", "Fax 2",
			"Email 1", "Email 2", "Company", "Department", "Sector", "Title",
			"Street 1", "Street 2", "City", "State", "Zip", "Country",
			"website 1", "website 2", "notes" };

	public static DefaultStreamedContent constructReportSheet(
			List<Object[]> report, Countries country, Sector sector)
			throws Exception {
		Workbook wb = new XSSFWorkbook();
		Map<String, CellStyle> styles = createStyles(wb);
		Sheet sheet = wb.createSheet("Report");

		// turn off gridlines
		sheet.setDisplayGridlines(true);
		sheet.setPrintGridlines(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		Row row;
		Row row1;
		row1 = sheet.createRow(0);
		row1.setHeightInPoints(12.75f);
		Cell cell;
		int rownum = 1;
		BusinessCard bc;
		// for header title
		if (sector == null) {
			composeCell(row1, styles.get("header"), "sector_id", 0);
			composeCell(row1, styles.get("header"), "sector_name", 1);
			composeCell(row1, styles.get("header"), "Number of BC", 2);
			sheet.setColumnWidth(0, 7000);
			sheet.setColumnWidth(1, 7000);
			sheet.setColumnWidth(2, 7000);
			for (int i = 0; i < report.size(); i++, rownum++) {
				row = sheet.createRow(rownum);
				row.setHeight((short) 700);
				int cellCount = 0;
				for (Object obj : report.get(i)) {
					if (obj instanceof Integer)
						composeCell(row, styles.get("cell_normal"),
								String.valueOf((int) obj), cellCount++);
					if (obj instanceof BigInteger)
						composeCell(row, styles.get("cell_normal"),
								String.valueOf((BigInteger) obj), cellCount++);
					if (obj instanceof String)
						composeCell(row, styles.get("cell_normal"),
								(String) obj, cellCount++);
				}
			}
		} else {
			composeCell(row1, styles.get("header"), "Business Card Title", 0);
			composeCell(row1, styles.get("header"), "No. of Business cards for the same title ", 1);
			
			sheet.setColumnWidth(0, 7000);
			sheet.setColumnWidth(1, 10000);
			
			for (int i = 0; i < report.size(); i++, rownum++) {
				row = sheet.createRow(rownum);
				row.setHeight((short) 700);
				int cellCount = 0;
				for (Object obj : report.get(i)) {
					if (obj == null )
						
					composeCell(row, styles.get("cell_normal"),
							"BC for the same sector with no country", cellCount++);
					if (obj instanceof Integer)
						composeCell(row, styles.get("cell_normal"),
								String.valueOf((int) obj), cellCount++);
					if (obj instanceof BigInteger)
						composeCell(row, styles.get("cell_normal"),
								String.valueOf((BigInteger) obj), cellCount++);
					if (obj instanceof String)
						composeCell(row, styles.get("cell_normal"),
								(String) obj, cellCount++);
				}
			}
		}
		// Write the output to a file

		String file = ResourceBundle.getBundle("settings").getString(
				"UPLOAD_PATH")
				+ ResourceBundle.getBundle("settings").getString(
						"BUFFER_UPLOAD_PATH")
				+ "REPORT_"
				+ Calendar.getInstance().getTime().toString().replace(" ", "")
						.replace(":", "") + ".xlsx";

		FileOutputStream out = new FileOutputStream(file);
		wb.write(out);
		out.close();
		return new DefaultStreamedContent(new FileInputStream(file), "xlsx");
	}

	/**
	 * @param exportList
	 * @return
	 * @throws IOException
	 */
	public static DefaultStreamedContent exportBusinessCardListToExcel(
			List<BusinessCard> exportList) throws IOException {
		Workbook wb = new XSSFWorkbook();
		Map<String, CellStyle> styles = createStyles(wb);
		Sheet sheet = wb.createSheet("Business Plan");

		// turn off gridlines
		sheet.setDisplayGridlines(true);
		sheet.setPrintGridlines(true);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		// yasser
		for (int x = 0; x < bcExportTitles.length; x++) {
			sheet.autoSizeColumn(x);
		}
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		// the header row: centered text in 48pt font
		Row headerRow = sheet.createRow(0);
		headerRow.setHeightInPoints(12.75f);
		for (int i = 0; i < bcExportTitles.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(bcExportTitles[i]);
			cell.setCellStyle(styles.get("header"));
		}

		// columns for 11 weeks starting from 9-Jul
		// Calendar calendar = Calendar.getInstance();
		// int year = calendar.get(Calendar.YEAR);
		//
		// calendar.setTime(fmt.parse("9-Jul"));
		// calendar.set(Calendar.YEAR, year);
		// for (int i = 0; i < 11; i++) {
		// Cell cell = headerRow.createCell(titles.length + i);
		// cell.setCellValue(calendar);
		// cell.setCellStyle(styles.get("header_date"));
		// calendar.roll(Calendar.WEEK_OF_YEAR, true);
		// }

		// freeze the first row
		sheet.createFreezePane(0, 1);

		Row row;

		Cell cell;
		int rownum = 1;
		BusinessCard bc;
		for (int i = 0; i < exportList.size(); i++, rownum++) {
			bc = exportList.get(i);
			row = sheet.createRow(rownum);
			// yasser
			row.setHeight((short) 700);
			if (bc == null)
				continue;
			int cellCount = 0;
			composeCell(row, styles.get("cell_normal"), bc.getBcFirstName(),
					cellCount++);
			composeCell(row, styles.get("cell_normal"), bc.getBcLastName(),
					cellCount++);
			Iterator it = bc.getMobiles().iterator();
			// mobile 1 & 2
			for (int j = 0; j < 2; j++) {
				if (it.hasNext())
					composeCell(row, styles.get("cell_normal"),
							((Mobile) it.next()).getId().getMobileNum(),
							cellCount++);
				else
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
			}
			// phone 1 & 2
			it = bc.getPhones().iterator();
			for (int j = 0; j < 2; j++) {
				if (it.hasNext())
					composeCell(row, styles.get("cell_normal"),
							((Phone) it.next()).getId().getPhoneNum(),
							cellCount++);
				else
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
			}
			// fax 1 & 2
			it = bc.getFaxes().iterator();
			for (int j = 0; j < 2; j++) {
				if (it.hasNext())
					composeCell(row, styles.get("cell_normal"),
							((Fax) it.next()).getId().getFaxNum(), cellCount++);
				else
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
			}
			// email 1 & 2
			it = bc.getEmails().iterator();
			for (int j = 0; j < 2; j++) {
				if (it.hasNext())
					composeCell(row, styles.get("cell_normal"),
							((Email) it.next()).getId().getEmailAddress(),
							cellCount++);
				else
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
			}
			// title 1 "Company", "Department", "Sector", "Title",
			it = bc.getTitles().iterator();
			for (int j = 0; j < 1; j++) {
				if (it.hasNext()) {
					Title t = (Title) it.next();
					if (t.getCompany() != null) {
						composeCell(row, styles.get("cell_normal"),
								t.getCompany(), cellCount++);
					}
					if (t.getDepartment() != null) {
						composeCell(row, styles.get("cell_normal"),
								t.getDepartment(), cellCount++);
					}
					if (t.getSector() != null) {
						composeCell(row, styles.get("cell_normal"), t
								.getSector().getSectorName(), cellCount++);
					}
					if (t.getTitle() != null) {
						composeCell(row, styles.get("cell_normal"),
								t.getTitle(), cellCount++);
					}
				} else {
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
				}

			}
			// address "Street 1", "Street 2", "City", "State", "Zip",
			// "Country",
			composeCell(row, styles.get("cell_normal"), bc.getAddress()
					.getStreet1(), cellCount++);
			composeCell(row, styles.get("cell_normal"), bc.getAddress()
					.getStreet2(), cellCount++);
			composeCell(row, styles.get("cell_normal"), bc.getAddress()
					.getCity(), cellCount++);
			composeCell(row, styles.get("cell_normal"), bc.getAddress()
					.getState(), cellCount++);
			composeCell(row, styles.get("cell_normal"), bc.getAddress()
					.getZip(), cellCount++);
			if (bc.getAddress().getCountries() != null)
				composeCell(row, styles.get("cell_normal"), bc.getAddress()
						.getCountries().getCountryName(), cellCount++);
			// website 1 & 2
			it = bc.getWebsites().iterator();
			for (int j = 0; j < 2; j++) {
				if (it.hasNext())
					composeCell(row, styles.get("cell_normal"),
							((Website) it.next()).getId().getWebsite(),
							cellCount++);
				else
					composeCell(row, styles.get("cell_normal"), "", cellCount++);
			}
			// "notes"
			composeCell(row, styles.get("cell_normal"), bc.getNotes(),
					cellCount++);

		}

		// Write the output to a file
		String file = ResourceBundle.getBundle("settings").getString(
				"UPLOAD_PATH")
				+ ResourceBundle.getBundle("settings").getString(
						"BUFFER_UPLOAD_PATH")
				+ "EXPORT_"
				+ Calendar.getInstance().getTime().toString().replace(" ", "")
						.replace(":", "") + ".xlsx";

		FileOutputStream out = new FileOutputStream(file);
		wb.write(out);
		out.close();
		return new DefaultStreamedContent(new FileInputStream(file), "xlsx",
				"export.xlsx");
	}

	private static void composeCell(Row row, CellStyle style, String value,
			int cellCount) {
		Cell cell = row.createCell(cellCount);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("header_date", style);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font1);
		styles.put("cell_b", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font1);
		styles.put("cell_b_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_b_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_g", style);

		Font font2 = wb.createFont();
		font2.setColor(IndexedColors.BLUE.getIndex());
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font2);
		styles.put("cell_bb", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_bg", style);

		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 14);
		font3.setColor(IndexedColors.DARK_BLUE.getIndex());
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font3);
		style.setWrapText(true);
		styles.put("cell_h", style);

		style = createBorderedStyle(wb);
		// yasser
		style.setAlignment(CellStyle.ALIGN_LEFT);
		// style.setWrapText(true);
		style.setShrinkToFit(true);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short) 1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);

		return styles;
	}

	private static CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}
}
