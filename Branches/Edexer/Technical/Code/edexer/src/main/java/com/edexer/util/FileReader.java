package com.edexer.util;

import java.io.File;
import java.util.List;

public interface FileReader {
	public static final String TXTEXTENSION = "txt";
	public static final String EXLEXTENSION = "xls";
	public static final String EXLXEXTENSION = "xlsx";
	
	
	public List<List<String>> read(File f);
	
	public List<List<String>> read(int sheet, int skipRows);
}
