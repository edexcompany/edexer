package com.edexer.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.faces.bean.ManagedProperty;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edexer.dao.CountriesEntityDaoImpl;
import com.edexer.dao.SectorEntityDaoImpl;
import com.edexer.dao.UserEntityDaoImpl;
import com.edexer.model.Address;
import com.edexer.model.BusinessCard;
import com.edexer.model.Countries;
import com.edexer.model.Email;
import com.edexer.model.EmailId;
import com.edexer.model.Fax;
import com.edexer.model.FaxId;
import com.edexer.model.Mobile;
import com.edexer.model.MobileId;
import com.edexer.model.Phone;
import com.edexer.model.PhoneId;
import com.edexer.model.Sector;
import com.edexer.model.Title;
import com.edexer.model.Website;
import com.edexer.model.WebsiteId;
import com.edexer.util.ExcelWorkBookFactory;

@Service("excelHandlerManager")
public class ExcelHandlerManagerImpl implements ExcelHandlerManager {

	private File file;
	public Workbook workbook;
	public FileInputStream fis;
	private int totalNumOfSheets;
	ResourceBundle settings = ResourceBundle.getBundle("settings");
	@Autowired
	private SectorEntityDaoImpl sectorDao;
	private Map<String, Integer> HEADER_LIST = new HashMap<String, Integer>();

	@Autowired
	private LookUpsServiceManager lookupService;

	@Autowired
	private AdminConfigManager adminConfigService;

	private static final Logger logger = Logger
			.getLogger(ExcelHandlerManagerImpl.class);

	// public ExcelHandlerManagerImpl(File f, String fileExt) throws Exception {
	// file = f;
	// FileInputStream fis = new FileInputStream(file);
	//
	// workbook = ExcelWorkBookFactory.getInstance(fis, fileExt);
	//
	// totalNumOfSheets = workbook.getNumberOfSheets();
	// }

	// public ExcelHandlerManagerImpl(String fileName) throws Exception {
	// this(new File(fileName), "");
	// }

	/**
	 * This method read excel files and ignore blank rows.
	 * 
	 * @param sheet
	 * @param skipRows
	 * @return
	 */
	public List<List<String>> read(int sheet, int skipRows) {
		return read(sheet, skipRows, false);

	}

	/**
	 * This method read multiple, specific sheets in the excel file
	 * 
	 * @param sheets
	 * @param skipRows
	 * @param readBlankRows
	 * @return
	 */
	public List<List<String>> read(int[] sheets, int skipRows) {
		List<List<String>> sheetContent = new ArrayList<List<String>>();
		for (int i : sheets) {
			sheetContent.addAll(read(i, skipRows));
		}
		return sheetContent;
	}

	/**
	 * This method reads ALL the sheets in the excel file.
	 * 
	 * @param skipRows
	 * @param readBlanRows
	 * @return
	 */
	public List<List<String>> read(int skipRows) {
		List<List<String>> sheetConetent = new ArrayList<List<String>>();
		for (int i = 0; i < totalNumOfSheets; i++) {
			sheetConetent.addAll(read(i, skipRows));
		}
		return sheetConetent;
	}

	/**
	 * This method reads ALL the sheets in the excel file, and take boolean to
	 * skip or not blank cells. This method created mainly to fix a problem in
	 * POS files, as there were rows with in between blank cells, which result
	 * in incorrect reading for data.
	 * 
	 * @param skipRows
	 *            int number of rows to skip
	 * @param readBlanRows
	 *            boolean to whether skip or not blank cells.
	 * @return
	 */
	public List<List<String>> read(int skipRows, boolean readBlankCells) {
		List<List<String>> sheetConetent = new ArrayList<List<String>>();
		for (int i = 0; i < totalNumOfSheets; i++) {
			sheetConetent.addAll(read(i, skipRows, readBlankCells));
		}
		return sheetConetent;
	}

	/**
	 * This method reads excel file and also consider blank rows.
	 * 
	 * @param sheet
	 * @param skipRows
	 * @return
	 */
	public List<List<String>> read(int sheet, int skipRows,
			boolean readBlankRows) {

		if (sheet < 0 || skipRows < 0) {
			throw new RuntimeException(
					"Invalid sheet number, or invalid rows to skip");
		}
		Sheet s = workbook.getSheetAt(sheet);
		ArrayList<String> curRecord = new ArrayList<>();
		List<List<String>> fileData = new ArrayList<List<String>>();
		int i = 0;
		System.out.println(" Last row in sheet #: " + sheet + " is "
				+ s.getLastRowNum());
		for (Row r : s) {
			if (i < skipRows) {
				i++;
				continue;
			}
			for (Cell cell : r) {
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					int d = (int) cell.getNumericCellValue();
					curRecord.add(String.valueOf(d));
				} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					curRecord.add(cell.getStringCellValue());
				} else if (cell.getCellType() == Cell.CELL_TYPE_BLANK
						&& readBlankRows) {
					curRecord.add(cell.getStringCellValue());
				}
			}
			if (!curRecord.isEmpty()) {
				fileData.add((ArrayList<String>) curRecord.clone());
			}
			curRecord.clear();
		}
		return fileData;
	}

	/**
	 * 
	 * @throws Exception
	 * @throws IOException
	 */
	public List<BusinessCard> readAndConvertToBusinessCard(int sheet,
			int skipRows, boolean readBlankRows, File f, String fileExt)
			throws Exception {
		if (!validateWorkbook(f, fileExt))
			return null;
		file = f;
		FileInputStream fis = new FileInputStream(file);
		workbook = ExcelWorkBookFactory.getInstance(fis, fileExt);
		totalNumOfSheets = workbook.getNumberOfSheets();
		List<BusinessCard> bcList = new ArrayList<BusinessCard>();
		Sheet s = workbook.getSheetAt(sheet);

		fillHeaderMap(s);
		int i = 0;
		BusinessCard bc;
		for (Row r : s) {
			if (i < skipRows) {
				i++;
				continue;
			}
			try {
				bc = createBusinessCardFromExcelRecord(r);
				bcList.add(bc);
			} catch (Exception e) {
				logger.warn("Recods can not be parced in excel sheet");
			}
		}

		return bcList;
	}

	/**
	 * This method construct a businesscard object from excel Row statically,
	 * assumming that the cells are predefined based on the sample the user will
	 * download.
	 * 
	 * @param r
	 * @return
	 */
	private BusinessCard createBusinessCardFromExcelRecord(Row r) {
		logger.debug("Row to be parsed:");
		logger.debug(r.toString());
		for (int i = 0; i < r.getLastCellNum(); i++) {
			try {
				r.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
			} catch (Exception e) {

			}
		}

		BusinessCard bc = new BusinessCard();
		// ////////////READING TITLE/////////////////
		Title t = new Title();
		String company = "", department = "", sector = "", title = "";
		try {
			company = r.getCell(10).getStringCellValue();
			t.setCompany(company);
		} catch (Exception e) {
			System.out.println("");
		}
		try {
			department = r.getCell(11).getStringCellValue();
			t.setDepartment(department);
		} catch (Exception e) {
			System.out.println("");
		}
		try {
			sector = r.getCell(12).getStringCellValue();
			Sector s = sectorDao.getsectorByName(sector);
			if (s != null)
				t.setSector(s);
		} catch (Exception e) {
			System.out.println("");
		}
		try {
			title = r.getCell(13).getStringCellValue();
			t.setTitle(title);
		} catch (Exception e) {
			System.out.println("");
		}
		Set titles = new HashSet();

		titles.add(t);
		bc.setTitles(titles);
		// //////////////////////////////////////////
		// //////////READING FirstName///////////////
		String fName = "", lName = "";
		try {
			fName = r.getCell(0).getStringCellValue();
		} catch (Exception e) {
			System.out.println("");
		}
		bc.setBcFirstName(fName);
		try {
			lName = r.getCell(1).getStringCellValue();
		} catch (Exception e) {
			System.out.println("");
		}
		bc.setBcLastName(lName);
		// //////////////////////////////////////////////////////
		// //////////READING Mobile_1 and Mobile_2///////////////

		Set<Mobile> mobiles = new HashSet<Mobile>();
		if (r.getCell(2) != null
				&& !r.getCell(2).getStringCellValue().equals("")
				&& r.getCell(2).getCellType() != Cell.CELL_TYPE_BLANK) {
			String mobile1 = r.getCell(2).getStringCellValue();
			if (mobile1 != null) {
				Mobile m = new Mobile();
				MobileId mId = new MobileId();
				mId.setMobileNum(mobile1);
				m.setId(mId);
				mobiles.add(m);
				bc.setMobiles(mobiles);
			}
		}
		if (r.getCell(3) != null
				&& !r.getCell(3).getStringCellValue().equals("")
				&& r.getCell(3).getCellType() != Cell.CELL_TYPE_BLANK) {

			String mobile2 = r.getCell(3).getStringCellValue();
			if (mobile2 != null) {
				Mobile m = new Mobile();
				MobileId mId = new MobileId();
				mId.setMobileNum(mobile2);
				m.setId(mId);
				mobiles.add(m);
				bc.setMobiles(mobiles);
			}
		}

		Set<Phone> phones = new HashSet<Phone>();
		if (r.getCell(4) != null
				&& !r.getCell(4).getStringCellValue().equals("")
				&& r.getCell(4).getCellType() != Cell.CELL_TYPE_BLANK) {

			String tele1 = r.getCell(4).getStringCellValue();
			if (tele1 != null) {
				Phone p1 = new Phone();
				PhoneId pId = new PhoneId();
				pId.setPhoneNum(tele1);
				p1.setId(pId);
				phones.add(p1);
				bc.setPhones(phones);
			}
		}
		if (r.getCell(5) != null
				&& !r.getCell(5).getStringCellValue().equals("")
				&& r.getCell(5).getCellType() != Cell.CELL_TYPE_BLANK) {

			String tele2 = r.getCell(5).getStringCellValue();

			if (tele2 != null) {
				Phone p2 = new Phone();
				PhoneId pId = new PhoneId();
				pId.setPhoneNum(tele2);
				p2.setId(pId);
				phones.add(p2);
				bc.setPhones(phones);
			}
		}

		// email
		Set<Email> emails = new HashSet<Email>();
		if (r.getCell(8) != null
				&& !r.getCell(8).getStringCellValue().equals("")
				&& r.getCell(8).getCellType() != Cell.CELL_TYPE_BLANK) {
			String email = r.getCell(8).getStringCellValue();
			if (email != null) {
				Email e = new Email();
				EmailId eId = new EmailId();
				eId.setEmailAddress(email);
				e.setId(eId);
				emails.add(e);
				bc.setEmails(emails);
			}
		}

		if (r.getCell(9) != null
				&& !r.getCell(9).getStringCellValue().equals("")
				&& r.getCell(9).getCellType() != Cell.CELL_TYPE_BLANK) {
			String email = r.getCell(9).getStringCellValue();
			if (email != null) {
				Email e = new Email();
				EmailId eId = new EmailId();
				eId.setEmailAddress(email);
				e.setId(eId);
				emails.add(e);
				bc.setEmails(emails);
			}
		}

		// Fax
		Set<Fax> faxes = new HashSet<Fax>();
		if (r.getCell(6) != null
				&& !r.getCell(6).getStringCellValue().equals("")
				&& r.getCell(6).getCellType() != Cell.CELL_TYPE_BLANK) {

			String fax = r.getCell(6).getStringCellValue();
			if (fax != null) {
				Fax f = new Fax();
				FaxId fId = new FaxId();
				fId.setFaxNum(fax);
				f.setId(fId);
				faxes.add(f);
				bc.setFaxes(faxes);
			}
		}
		if (r.getCell(7) != null
				&& !r.getCell(7).getStringCellValue().equals("")
				&& r.getCell(7).getCellType() != Cell.CELL_TYPE_BLANK) {

			String fax = r.getCell(7).getStringCellValue();
			if (fax != null) {
				Fax f = new Fax();
				FaxId fId = new FaxId();
				fId.setFaxNum(fax);
				f.setId(fId);
				faxes.add(f);
				bc.setFaxes(faxes);
			}
		}

		// website
		Set<Website> websites = new HashSet<Website>();
		if (r.getCell(20) != null
				&& !r.getCell(20).getStringCellValue().equals("")
				&& r.getCell(20).getCellType() != Cell.CELL_TYPE_BLANK) {
			String website = r.getCell(20).getStringCellValue();
			if (website != null) {
				Website web = new Website();
				WebsiteId wId = new WebsiteId();
				wId.setWebsite(website);
				web.setId(wId);
				websites.add(web);
				bc.setWebsites(websites);
			}
		}

		if (r.getCell(21) != null
				&& !r.getCell(21).getStringCellValue().equals("")
				&& r.getCell(21).getCellType() != Cell.CELL_TYPE_BLANK) {
			String website = r.getCell(21).getStringCellValue();
			if (website != null) {
				Website web = new Website();
				WebsiteId wId = new WebsiteId();
				wId.setWebsite(website);
				web.setId(wId);
				websites.add(web);
				bc.setWebsites(websites);
			}
		}

		// Address
		Address address = new Address();
		if (r.getCell(14) != null
				&& r.getCell(14).getCellType() != Cell.CELL_TYPE_BLANK)
			address.setStreet1((r.getCell(14).getStringCellValue() != null) ? r
					.getCell(14).getStringCellValue() : "");

		if (r.getCell(15) != null
				&& r.getCell(15).getCellType() != Cell.CELL_TYPE_BLANK)
			address.setStreet2((r.getCell(15).getStringCellValue() != null) ? r
					.getCell(15).getStringCellValue() : "");
		if (r.getCell(16) != null
				&& r.getCell(16).getCellType() != Cell.CELL_TYPE_BLANK)
			address.setCity((r.getCell(16).getStringCellValue() != null) ? r
					.getCell(16).getStringCellValue() : "");
		if (r.getCell(17) != null
				&& r.getCell(17).getCellType() != Cell.CELL_TYPE_BLANK)
			address.setState((r.getCell(17).getStringCellValue() != null) ? r
					.getCell(17).getStringCellValue() : "");
		if (r.getCell(18) != null
				&& r.getCell(18).getCellType() != Cell.CELL_TYPE_BLANK)
			address.setZip((r.getCell(18).getStringCellValue() != null) ? r
					.getCell(18).getStringCellValue() : "");
		Countries c = new Countries();
		c.setIdCountry(5);
		address.setCountries(c);
		if (r.getCell(19) != null
				&& r.getCell(19).getCellType() != Cell.CELL_TYPE_BLANK) {
			String countryName = r.getCell(19).getStringCellValue();
			c = lookupService.getCountryByName(countryName);
			address.setCountries(c);
		}

		// TO DO: set countries and sectors from db
		bc.setAddress(address);

		// Notes
		if (r.getCell(22) != null
				&& r.getCell(22).getCellType() != Cell.CELL_TYPE_BLANK) {
			String notes = r.getCell(22).getStringCellValue();
			if (notes != null) {
				bc.setNotes(notes);
			}
		}

		return bc;
	}

	/**
	 * this is a helper method that fill the HEADER_MAP map to know each column
	 * map to which value.
	 * 
	 * @param s
	 */
	private void fillHeaderMap(Sheet s) {
		Row firstRow = s.getRow(0);
		int cellIndex = 0;
		for (Cell c : firstRow) {
			if (c.getCellType() == Cell.CELL_TYPE_STRING) {
				HEADER_LIST.put(c.getStringCellValue(), cellIndex++);
			}
		}
	}

	public void clear() throws IOException {
		workbook = null;
	}

	public Map<String, Integer> getHEADER_LIST() {
		return HEADER_LIST;
	}

	public LookUpsServiceManager getLookupService() {
		return lookupService;
	}

	public void setLookupService(LookUpsServiceManager lookupService) {
		this.lookupService = lookupService;
	}

	@Override
	public boolean validateWorkbook(File file, String fileExt) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		workbook = ExcelWorkBookFactory.getInstance(fis, fileExt);
		totalNumOfSheets = workbook.getNumberOfSheets();
		Sheet s = workbook.getSheetAt(0/* first sheet */);
		int max = Integer.valueOf(adminConfigService
				.getAdminConfigValueByKey("WORKBOOK_UPLOAD_MAX"));
		return (max > s.getLastRowNum() ? true : false);
	}

	public AdminConfigManager getAdminConfigService() {
		return adminConfigService;
	}

	public void setAdminConfigService(AdminConfigManager adminConfigService) {
		this.adminConfigService = adminConfigService;
	}

}
