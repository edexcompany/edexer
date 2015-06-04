/*package com.edexer.util;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.edexer.model.BusinessCard;
import com.edexer.model.Mobile;

public class ExcelReaderTest {

	URL url = this.getClass().getResource("/contacts.xlsx");
	File contacts = new File(url.getFile());
	
	
	@Test
	public void test() throws Exception {
		ExcelReader r = new ExcelReader(contacts,"xlsx");
		List<BusinessCard> l = r.readAndConvertToBusinessCard(0, 1, false);
		System.out.println(r.getHEADER_LIST());
		System.out.println("-------------");
		for(int i=0; i < l.size() ; i++){
			System.out.print(l.get(i).getBcFirstName() + " = ");
			Iterator it = l.get(i).getMobiles().iterator();
			while(it.hasNext()){
				System.out.println(((Mobile)it.next()).getId().getMobileNum());
			}
		}
		
	}

}
*/