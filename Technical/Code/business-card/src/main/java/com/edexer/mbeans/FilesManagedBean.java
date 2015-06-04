package com.edexer.mbeans;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;

import com.edexer.dao.AttachmentEntityDaoImpl;
import com.edexer.model.Attachment;

@ManagedBean
@ViewScoped
public class FilesManagedBean implements Serializable {

	@Autowired
	private AttachmentEntityDaoImpl attachmentDao;
	
	private UploadedFile file;


	public FilesManagedBean() {

	}

	public void upload() throws IOException {
		if (getFile() != null) {
			FacesMessage message = new FacesMessage("Succesful", getFile()
					.getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, message);

			FileInputStream in=null;
			FileOutputStream out=null;
			try {
				//attachmentDao.add(new Attachment(id, businessCard, getFile().getFileName()));
				in = (FileInputStream) getFile().getInputstream();
				out = new FileOutputStream("E://file");

				int c;
				while ((c = in.read()) != -1) {
					out.write(c);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

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

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public AttachmentEntityDaoImpl getAttachmentDao() {
		return attachmentDao;
	}

	public void setAttachmentDao(AttachmentEntityDaoImpl attachmentDao) {
		this.attachmentDao = attachmentDao;
	}



}
