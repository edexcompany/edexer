package com.edexer.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

import org.primefaces.model.UploadedFile;

import com.edexer.mbeans.Constants;

public class FilesUtil {
	private static ResourceBundle settingsBundle = ResourceBundle
			.getBundle("settings");

	public static boolean uploadFile(UploadedFile file, int attachmentId)
			throws IOException {

		System.out.println("file name: " + file.getFileName()+", folder name: "+attachmentId);
		InputStream in = null;
		FileOutputStream out = null;
		try {
			System.out.println("getting streams");
			in =  file.getInputstream();
			File dir = new File(settingsBundle.getString("UPLOAD_PATH")
					+ attachmentId);
			dir.mkdirs();
			System.out.println("dir made");
			out = new FileOutputStream(new File(dir, file.getFileName()));
			int c;
			System.out.println("writing the file");
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			System.out.println("file written");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error while uploading the file");
			return false;
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

}
