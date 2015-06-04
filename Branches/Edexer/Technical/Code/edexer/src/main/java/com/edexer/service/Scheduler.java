package com.edexer.service;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");
	@Scheduled(fixedRate = 86400)
	public void freeTempFiles() {

		File targetFolder = new File(settingsBundle.getString("UPLOAD_PATH")
				+ settingsBundle.getString("BUFFER_UPLOAD_PATH"));
		try {
			FileUtils.cleanDirectory(targetFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
