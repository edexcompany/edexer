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
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.edexer.dao.CountriesEntityDaoImpl;
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
import com.edexer.service.LookUpsServiceManager;

public interface ExcelHandlerManager {
	public List<BusinessCard> readAndConvertToBusinessCard(int sheet,
			int skipRows, boolean readBlankRows,File f, String fileExt) throws Exception;
	
	public boolean validateWorkbook(File file,String fileExt) throws Exception;
}
