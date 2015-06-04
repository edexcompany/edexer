package com.edexer.service;

import java.util.List;

import com.edexer.model.Tags;
import com.edexer.model.User;

public interface TagsServiceManager {

	public Integer addTag(Tags t);
	
	public void updateTag(Tags t);

	public List<Tags> getPersonalTagsList(User user);

	public List<Tags> getCorporateTagsList(User user);
	
	public Tags get(int id);

	public void delete(Tags t);
}
