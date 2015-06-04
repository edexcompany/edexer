package com.edexer.servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/imagesPath/*")
public class ImageServlet extends HttpServlet {

	private ResourceBundle settingsBundle;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        settingsBundle = ResourceBundle.getBundle("/settings");
    	String filename = request.getPathInfo().substring(1);
        File file = new File(settingsBundle.getString("UPLOAD_PATH"), filename);
        try{
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        Files.copy(file.toPath(), response.getOutputStream());
        }catch(Exception e){
        	filename = "common/avatar.png";
        	file = new File(settingsBundle.getString("UPLOAD_PATH"), filename);
        	response.setHeader("Content-Type", getServletContext().getMimeType(filename));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
        	Files.copy(file.toPath(), response.getOutputStream());
        }
    }

}
