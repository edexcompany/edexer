package com.edexer.service;

import java.io.IOException;

import org.primefaces.model.StreamedContent;

import com.edexer.model.Countries;
import com.edexer.model.Sector;
import com.edexer.model.UserSubscription;

public interface ReportingServicesManager {
	/**
	 * Returns excelsheet containing number of business cards for each sector
	 * given a user subscription
	 * 
	 * @param us
	 *            : the user subscription to run the report over
	 * @return Excelsheet contining the results
	 * @throws Exception
	 */
	public StreamedContent getBCCountPerSector(UserSubscription us)
			throws Exception;

	/**
	 * Returns excelsheet containing number of business cards for each sector
	 * given a user subscription and country
	 * 
	 * @param us
	 *            : the user subscription to run the report over
	 * @param country
	 *            : the country to run the report over
	 * @return Excelsheet contining the results
	 * @throws Exception
	 */
	public StreamedContent getBCCountPerSector(UserSubscription us,
			Countries country) throws Exception;

	/**
	 * Returns excelsheet containing number of repeatition of each title given a
	 * sector or both sector and country
	 * 
	 * @param us
	 *            : the user subscription to run the report over
	 * @param country
	 *            : the country to run the report over
	 * @param sector
	 *            : the sector to run the report over, it is required
	 * @return Excelsheet contining the results
	 * @throws Exception
	 */
	public StreamedContent getTitleCountGivenCountryAndSector(
			UserSubscription us, Countries country, Sector sector)
			throws Exception;
}
