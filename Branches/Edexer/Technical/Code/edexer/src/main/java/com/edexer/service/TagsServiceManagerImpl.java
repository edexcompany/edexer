package com.edexer.service;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edexer.dao.TagsEntityDaoImpl;
import com.edexer.mbeans.Constants;
import com.edexer.model.Tags;
import com.edexer.model.User;

@Service("tagsServiceManager")
@Transactional
public class TagsServiceManagerImpl implements TagsServiceManager {

	@Autowired
	TagsEntityDaoImpl tagsDao;

	ResourceBundle settingsBundle = ResourceBundle.getBundle("settings");
	
	@Override
	public Integer addTag(Tags t) {
		return (Integer) tagsDao.add(t);
	}
	
	@Override
	public void updateTag(Tags t) {
		tagsDao.update(t);
	}

	@Override
	public List<Tags> getPersonalTagsList(User user) {
		return tagsDao.getTagsList(user,false);
	}

	@Override
	public List<Tags> getCorporateTagsList(User user) {
		return tagsDao.getTagsList(user, true);
	}

	@Override
	public Tags get(int id) {
		return tagsDao.get(Tags.class, id);
	}
	
	@Override
	public void delete(Tags t){
		tagsDao.delete(t);
	}
	

}
