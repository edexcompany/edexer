package com.edexer.service;

import java.io.IOException;

import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.ReportingDaoImpl;
import com.edexer.model.Countries;
import com.edexer.model.Sector;
import com.edexer.model.UserSubscription;
import com.edexer.util.ExcelExporter;

@Transactional
@Service("reportingServiceManager")
public class ReportingServiceManagerImpl implements ReportingServicesManager {

	@Autowired
	private ReportingDaoImpl reportingDao;

	@Override
	public StreamedContent getBCCountPerSector(UserSubscription us)
			throws Exception {
		return ExcelExporter.constructReportSheet(reportingDao.runReport(us
				.getId().getUserId(), us.getId().getSubType(), null, null),null,null);
	}

	@Override
	public StreamedContent getBCCountPerSector(UserSubscription us,
			Countries country) throws Exception {
		return ExcelExporter.constructReportSheet(reportingDao.runReport(us
				.getId().getUserId(), us.getId().getSubType(), country, null),country,null);
	}

	@Override
	public StreamedContent getTitleCountGivenCountryAndSector(
			UserSubscription us, Countries country, Sector sector)
			throws Exception {
		return ExcelExporter
				.constructReportSheet(reportingDao.runReport(us.getId()
						.getUserId(), us.getId().getSubType(), country, sector),country,sector);

	}

	public ReportingDaoImpl getReportingDao() {
		return reportingDao;
	}

	public void setReportingDao(ReportingDaoImpl reportingDao) {
		this.reportingDao = reportingDao;
	}

}
